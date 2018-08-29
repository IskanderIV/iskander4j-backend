// Controller
// (c) Ivanov Aleksandr, 2018

#include "Controller.h"
#include "MenuBuilder.h"

#include "RFManager.h"
#include "WifiManager.h"
#include "DataBase.h"
#include "Event.h"

#include "MenuSelectorEventGenerator.h"
#include "DisplayEventGenerator.h"
#include "InputerEventGenerator.h"
#include "ControllerSelectorObserver.h"
#include "ControllerDisplayObserver.h"
#include "ControllerInputerObserver.h"
#include "ControllerChooserInterface.h"

Controller::Controller() {
	Serial.println("Controller()!");//TEST
	_mnSelectorEG = new MenuSelectorEventGenerator();
	_displayEG = new DisplayEventGenerator();
	_inputerEG = new InputerEventGenerator();
	init();	
}

Controller::~Controller(){
	delete _mnSelectorEG;
	delete _displayEG;
	delete _inputerEG;
	// TODO
}

void Controller::processLoop() {
	if (!_mnSelector) return;
	if (!_display) return;
	if (!_inputer) return;
	if (!_btnManager) return; 
	ButtonPin pushedBtnCode = obtainPushedBtnCode();
	if (bShouldShowMenu) {
		pushedBtnCode = btn_menu;
		bShouldShowMenu = false;
	}
	bool isBtnPushed = pushedBtnCode;
	//Serial.print("pushedBtnCode >> "); Serial.println(pushedBtnCode); //TEST
	if (!_mnSelector->isActive() && !_inputer->isActive() && !_chooser->isActive()) {
		//Serial.println("Selector is not active! _inputer is not active!");
		if (pushedBtnCode == btn_menu) {
			Serial.println("pushedBtnCode == btn_menu");
			_mnSelectorEG->notifyMenuSelectorToBeShowen();
			_displayEG->notifyDisplayToShowMenu();
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
		} else {
			Serial.println("----------------------------------------Work state!");
			quizeDevices();
			sendHttpRequest();
		}		
	} else if (_mnSelector->isActive()) {
		//Serial.println("Selector is active!");
		goWithMenuSelector(pushedBtnCode);
		//_displayEG->notifyDisplayToShowCurrMenu();
	} else if (_inputer->isActive()) {
		//Serial.println("_inputer is active!");
		goWithInputer(pushedBtnCode);
	} else if (_chooser->isActive()) {
		//Serial.println("_inputer is active!");
		goWithChooser(pushedBtnCode);
	} 
}

/**********
* privates
***********/

void Controller::init() {
	_mnSelector = NULL;
	_display = NULL;
	_inputer = NULL;
	_btnManager = NULL;
	_eepromManager = NULL;
	_rfManager = NULL;
	_wifiManager = NULL;
	_dataBase = NULL;
	_menuToActionMap = NULL;
	_isWifiConnectionOk = false;
}

void Controller::initWifi() {
	if (_wifiManager) {
		if (_wifiManager->connectToWifi()) {
			_isWifiConnectionOk = true;			
		} else {
			_isWifiConnectionOk = false;
		}		
		//_display->showConnectionToWifiMsg(isWifiOk);
	}
	delay(2000);
}

void Controller::quizeDevices() {
	if (_rfManager) {
		_rfManager->setActive(true);
		_rfManager->processDeveices();
		_rfManager->setActive(false);
	}
}

void Controller::sendHttpRequest() {
	if (_wifiManager && _isWifiConnectionOk) {
		_wifiManager->setActive(true);
		if (!_wifiManager->executePutRequest()) {
			_wifiManager->closeConnection();
		}
		_wifiManager->setActive(false);
	} else {
		initWifi();
	}
}

void Controller::goWithMenuSelector(ButtonPin pPushedBtnCode) {
	switch (pPushedBtnCode) 
	{
		case btn_menu:
			Serial.println("Menu is pushed!");//TEST
			_mnSelectorEG->notifyMenuSelectorToGoBack();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
		case btn_up:
			Serial.println("Menu Up is pushed!");//TEST
			Serial.println(_eepromManager->fetch(eepr_baseId));//TEST		
			Serial.println(_eepromManager->fetch(eepr_wifiLogin));//TEST
			Serial.println(_eepromManager->fetch(eepr_wifiPsswd));//TEST
			Serial.println(_eepromManager->fetch(eepr_serverAdress));//TEST
			Serial.println(_eepromManager->fetch(eepr_serverPort));//TEST
			_mnSelectorEG->notifyMenuSelectorToMoveUp();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
		case btn_down:
			Serial.println("Menu Down is pushed!");//TEST
			_mnSelectorEG->notifyMenuSelectorToMoveDown();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
        case btn_select:
			Serial.println("Select is pushed!");//TEST
			_mnSelectorEG->notifyMenuSelectorToSelect();
			if (_mnSelector->isActive()) {
				_displayEG->notifyDisplayToShowCurrMenu();
			}
			break;
    }	
}

void Controller::goWithInputer(ButtonPin pPushedBtnCode) {
	switch (pPushedBtnCode) {
		case btn_menu: {
			Serial.println("Menu is pushed!");//TEST
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_inputer->setActive(false);						
			break;
        }
		case btn_left: {
			Serial.println("Left is pushed!");//TEST
			_inputerEG->notifyInputerToMoveCursorLeft();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_right: {
			Serial.println("Right is pushed!");//TEST
			_inputerEG->notifyInputerToMoveCursorRight();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_up: {
			Serial.println("Inp Up is pushed!");//TEST
			_inputerEG->notifyInputerToChangeSymbUp();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_down: {
			Serial.println("Inp Down is pushed!");//TEST
			_inputerEG->notifyInputerToChangeSymbDown();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_select: {
			Serial.println("Select is pushed!");//TEST
			_inputerEG->notifyInputerToSaveText();
			String menuName = _mnSelector->getCurrMenuName();
			String savedText = _inputer->getSavedText();
			saveInputTextToEeprom(menuName, savedText);
			_displayEG->notifyDisplayToShowMenu();
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_inputer->setActive(false);
			break;
        }
    }
}

void Controller::goWithChooser(ButtonPin pPushedBtnCode) {
	switch (pPushedBtnCode) {
		case btn_menu: {
			Serial.println("Menu is pushed!");//TEST
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_chooser->setActive(false);						
			break;
        }
		case btn_left: {
			Serial.println("Left is pushed!");//TEST
			break;
        }
		case btn_right: {
			Serial.println("Right is pushed!");//TEST
			break;
        }
		case btn_up: {
			Serial.println("Cho Up is pushed!");//TEST
			_chooser->moveBack();
			_displayEG->notifyDisplayToShowCurrChooserElement();
			break;
        }
		case btn_down: {
			Serial.println("Cho Down is pushed!");//TEST
			_chooser->moveForward();
			_displayEG->notifyDisplayToShowCurrChooserElement();
			break;
        }
		case btn_select: {
			Serial.println("Select is pushed!");//TEST
			String menuName = _mnSelector->getCurrMenuName();
			String savedText = _chooser->getCurrElement();
			saveInputTextToEeprom(menuName, savedText);
			_displayEG->notifyDisplayToShowMenu();
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_chooser->setActive(false);
			break;
        }
    }
}

void Controller::saveInputTextToEeprom(String& pCurrMenuName, String& pSavedText) {
	_eepromManager->save(mapActionToEepromPlace(_menuToActionMap[pCurrMenuName]), pSavedText);
}

EepromPlaceName Controller::mapActionToEepromPlace(Action pAction) {
	switch (pAction) {
		case act_INPUT_WIFI_SSID: 	return eepr_wifiLogin;
		case act_INPUT_WIFI_PSWD: 	return eepr_wifiPsswd;
		case act_INPUT_TCP_LOGIN: 	return eepr_tcpLogin;
		case act_INPUT_TCP_PSWD: 	return eepr_tcpPsswd;
		case act_INPUT_SERVER_IP: 	return eepr_serverAdress;
		case act_INPUT_SERVER_PORT: return eepr_serverPort;
		case act_SELECT_WIFI: 		return eepr_wifiLogin;
    }
}

ButtonPin Controller::obtainPushedBtnCode() {
	ButtonPin* buttonList = _btnManager->processButtons();
	return buttonList[0]; // logic of selection only 1 button is here
}

/*
 SelectorControllerObserver interface
*/

void Controller::executeAction() {
	String currMenuName = _mnSelector->getCurrMenuName();
	Action pAction = _menuToActionMap[currMenuName];	
	switch (pAction) {
		case act_INPUT_WIFI_SSID:
		case act_INPUT_WIFI_PSWD:
		case act_INPUT_TCP_LOGIN:
		case act_INPUT_TCP_PSWD:
		case act_INPUT_SERVER_IP:
		case act_INPUT_SERVER_PORT: {
			int maxInputerTextLen = selectMaxInputerTextLen(pAction);
			runInputer(maxInputerTextLen);
			break;
        }
		case act_SELECT_WIFI: {
			String wifiNames[_wifiManager->getMaxFindedWANsCount()];
			_wifiManager->fetchFindedWANs(wifiNames);
			int wifiNamesCount = _wifiManager->getFindedWANsCount();
			Serial.print("Finded wifis after wfManager search>>> ");
			for (int i=0; i<wifiNamesCount; i++) {
				Serial.println(wifiNames[i]);
			}
			Serial.print("Count of wifis after wfManager search>>> ");
			Serial.println(wifiNamesCount);
			runChooser(wifiNames, wifiNamesCount);
			break;
        }
		case act_RESET_WIFI: {
			doBeforeSingleAction();
			//_displayEG->notifyDisplayToShowSmthDuringReset();
			initWifi();
			doAfterSingleAction();
			break;
        }
		case act_RESET_GSM: {
			
			break;
        }
		case act_SEARCH_DEVICES: {
			doBeforeSingleAction();
			searhDevices();			
			doAfterSingleAction();
			break;
        }
    }
}

void Controller::processMenuExit() {
	if (_mnSelector) {
		_mnSelector->setActive(false);
	}
	//_displayEG->notifyDisplayToShowWorkState();
}

/**********
* privates
***********/

void Controller::searhDevices() {
	ButtonPin pushedBtnCode;	
	do {
		if (_rfManager->searchDevices()) {
			_dataBase->saveDevicesIdsToEeprom();
			//TODO display good adding to memory
		}
		delay(100);
		pushedBtnCode = obtainPushedBtnCode();
	} 
	while (pushedBtnCode != btn_menu);
	
}

int Controller::selectMaxInputerTextLen(Action pAction) {
	return _eepromManager->getMaxByteOfPlace(mapActionToEepromPlace(pAction));
}

void Controller::runInputer(int pMaxInputerTextLen) {
	_displayEG->notifyDisplayToShowInputer();
	String maxInputerTextLen = String(pMaxInputerTextLen);
	Event* eventWithMaxInputLen = new Event(maxInputerTextLen);
	_inputerEG->notifyInputerToClear(eventWithMaxInputLen);
	delete eventWithMaxInputLen;
	_displayEG->notifyDisplayToShowCurrInputSymbol();
	_mnSelector->setActive(false);
	_inputer->setActive(true);
}

void Controller::runChooser(String* pWifiNames, int pCount) {
	_displayEG->notifyDisplayToShowChooser();
	_chooser->reinit(pWifiNames, pCount);
	_displayEG->notifyDisplayToShowCurrChooserElement();
	_mnSelector->setActive(false);
	_chooser->setActive(true);
	Serial.println("HERE Controller::runChooser end");
}

void Controller::doBeforeSingleAction() {	
	_mnSelector->setActive(false);
}

void Controller::doAfterSingleAction() {	
	_displayEG->notifyDisplayToShowMenu();
	_displayEG->notifyDisplayToShowCurrMenu();
	_mnSelector->setActive(true);
}

/*
 Other interface
*/

void Controller::setMenuSelector(ControllerSelectorObserver* pMnSelector) {
	_mnSelector = pMnSelector;
	if (_mnSelector) {
		_mnSelectorEG->addListener(_mnSelector);
	}	
}

void Controller::setDisplay(ControllerDisplayObserver* pDisplay) {
	_display = pDisplay;
	if (_display) {
		_displayEG->addListener(_display);
	}
}

void Controller::setInputer(ControllerInputerObserver* pInputer) {
	_inputer = pInputer;
	if (_inputer) {
		_inputerEG->addListener(_inputer);
	}
}

void Controller::setChooser(ControllerChooserInterface* pChooser) {
	_chooser = pChooser;
}

void Controller::setBtnManager(ButtonsManager* pBtnManager) {
	_btnManager = pBtnManager;
}

void Controller::setEepromManager(EepromManager* pEepromManager) {
	_eepromManager = pEepromManager;
	
	String uniqID_str = String(UNIQ_BASE_ID);//STUB
	_eepromManager->save(eepr_baseId, uniqID_str);//STUB need to get from server when registration
	
	uint8_t devIds[8] = {1,2,0,0,0,0,0,0};//STUB
	_eepromManager->saveDevicesIds(devIds);//STUB
	//String wifiLogin = String("acer Liquid Z630"); 
	String wifiLogin = String("RAZVRAT_HOUSE");//STUB
	//String wifiPsswd = String("111222333");
	String wifiPsswd = String("LaserJet");//STUB
	_eepromManager->save(eepr_wifiLogin, wifiLogin);//STUB
	_eepromManager->save(eepr_wifiPsswd, wifiPsswd);//STUB
	String tcpServerIP = String("192.168.1.34");//STUB
	String tcpServerPort = String("8090");//STUB
	_eepromManager->save(eepr_serverAdress, tcpServerIP);//STUB
	_eepromManager->save(eepr_serverPort, tcpServerPort);//STUB
}

void Controller::setRFManager(RFManager* pRfManager) {
	_rfManager = pRfManager;
}

void Controller::setDataBase(DataBase* pDataBase) {
	_dataBase = pDataBase;
	//_dataBase->setUniqBaseID(UNIQ_BASE_ID);
	
	//TODO fill DB from EEprom
}

void Controller::setWifiManager(WifiManager* pWifiManager) {
	Serial.println("Controller::setWifiManager initWifi()");
	_wifiManager = pWifiManager;
	initWifi();
}

void Controller::setMenuToActionMap(HashMap<String, Action, 12/*unsigned int*/> pMenuToActionMap) {
	_menuToActionMap = pMenuToActionMap;
}