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

void Controller::init() {
	_mnSelector = NULL;
	_display = NULL;
	_inputer = NULL;
	_btnManager = NULL;
	_rfManager = NULL;
	_wifiManager = NULL;
	_dataBase = NULL;
	_menuToActionMap = NULL;
	_isWifiConnectionOk = false;
}

/****************
* public methods*
*****************/

void Controller::processLoop() {
	if (!_mnSelector) return;
	if (!_display) return;
	if (!_inputer) return;
	if (!_btnManager) return; 
	ButtonPin pushedBtnCode = obtainPushedBtnCode();
	if (WAS_MENU_BTN_PRESSED) {
		pushedBtnCode = btn_menu;
		WAS_MENU_BTN_PRESSED = false;
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
			sendToServer(DATA);
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

void Controller::sendToServer(HttpExchangeType type) {
	int repeats = 0;
	uint8_t numOfrepeats = 1;
	do
	{
		if (_wifiManager && _isWifiConnectionOk) {
			_wifiManager->setActive(true);
			if (!_wifiManager->executeRequest(type)) {
				_wifiManager->closeConnection();
			}
			_wifiManager->setActive(false);
		} else {
			repeats++;
			initWifi();
		}
	while (!_isWifiConnectionOk && repeats < numOfrepeats);
}

void Controller::goWithMenuSelector(ButtonPin pPushedBtnCode) {
	switch (pPushedBtnCode) 
	{
		case btn_menu:
			Serial.println(F("Menu is pushed!"));//TEST
			_mnSelectorEG->notifyMenuSelectorToGoBack();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
		case btn_up:
			Serial.println(F("Menu Up is pushed!"));//TEST
			_mnSelectorEG->notifyMenuSelectorToMoveUp();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
		case btn_down:
			Serial.println(F("Menu Down is pushed!"));//TEST
			_mnSelectorEG->notifyMenuSelectorToMoveDown();
			_displayEG->notifyDisplayToShowCurrMenu();
			break;
        case btn_select:
			Serial.println(F("Select is pushed!"));//TEST
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
			Serial.println(F("Menu is pushed!"));//TEST
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_inputer->setActive(false);						
			break;
        }
		case btn_left: {
			Serial.println(F("Left is pushed!"));//TEST
			_inputerEG->notifyInputerToMoveCursorLeft();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_right: {
			Serial.println(F("Right is pushed!"));//TEST
			_inputerEG->notifyInputerToMoveCursorRight();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_up: {
			Serial.println(F("Inputer Up is pushed!"));//TEST
			_inputerEG->notifyInputerToChangeSymbUp();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_down: {
			Serial.println(F("Inputer Down is pushed!"));//TEST
			_inputerEG->notifyInputerToChangeSymbDown();
			_displayEG->notifyDisplayToShowCurrInputSymbol();
			break;
        }
		case btn_select: {
			Serial.println(F("Select is pushed!"));//TEST
			_inputerEG->notifyInputerToSaveText();
			String menuName = _mnSelector->getCurrMenuName();
			String savedText = _inputer->getSavedText();
			saveInputText(_menuToActionMap[menuName], savedText);
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
			Serial.println(F("Menu is pushed!"));//TEST
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_chooser->setActive(false);						
			break;
        }
		case btn_left: {
			// do not used, just for logging
			Serial.println(F("Left is pushed!"));//TEST
			break;
        }
		case btn_right: {
			// do not used, just for logging
			Serial.println(F("Right is pushed!"));//TEST
			break;
        }
		case btn_up: {
			Serial.println(F("Up is pushed!"));//TEST
			_chooser->moveBack();
			_displayEG->notifyDisplayToShowCurrChooserElement();
			break;
        }
		case btn_down: {
			Serial.println(F("Down is pushed!"));//TEST
			_chooser->moveForward();
			_displayEG->notifyDisplayToShowCurrChooserElement();
			break;
        }
		case btn_select: {
			Serial.println(F("Select is pushed!"));//TEST
			String menuName = _mnSelector->getCurrMenuName();
			String savedText = _chooser->getCurrElement();
			saveInputText(_menuToActionMap[menuName], savedText);
			_displayEG->notifyDisplayToShowMenu();
			_displayEG->notifyDisplayToShowCurrMenu();
			_mnSelector->setActive(true);
			_chooser->setActive(false);
			break;
        }
    }
}

void Controller::saveInputText(Action pAction, String& pSavedText) {
	switch (pAction) {
		case act_INPUT_WIFI_SSID: 		_dataBase->setSSID(pSavedText); break;
		case act_INPUT_WIFI_PSWD: 		_dataBase->setSsidPassword(pSavedText); break;
		case act_INPUT_TCP_LOGIN: 		_dataBase->setLogin(pSavedText); break;
		case act_INPUT_TCP_PSWD: 		_dataBase->setPassword(pSavedText); break;
		case act_INPUT_SERVER_IP: 		_dataBase->setHost(pSavedText); break;
		case act_INPUT_SERVER_PORT: 	_dataBase->setPort(pSavedText); break;
		case act_SELECT_WIFI: 			_dataBase->setLogin(pSavedText); break;
		case act_INPUT_SERVER_TARGET: 	_dataBase->setTarget(pSavedText); break;
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
		case act_INPUT_SERVER_PORT:
		case act_INPUT_SERVER_TARGET:
		case act_INPUT_BOARD_UID: { //TODO 1111111111111111111111
			int maxInputerTextLen = selectMaxInputerTextLen(pAction);
			activateInputer(maxInputerTextLen); 
			break;
        }
		case act_SELECT_WIFI: {
			String wifiNames[_wifiManager->getMaxFindedWANsCount()];
			_wifiManager->fetchFindedWANs(wifiNames);
			int wifiNamesCount = _wifiManager->getFindedWANsCount();
			Serial.print(F("Found wifis after wfManager search>>> ")); //TEST
			for (int i=0; i<wifiNamesCount; i++) {//TEST
				Serial.println(wifiNames[i]);//TEST
			}//TEST
			Serial.println(String(F("Count of wifis after wfManager search>>> ")) + wifiNamesCount);//TEST
			activateChooser(wifiNames, wifiNamesCount);
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
		case act_REGISTER_BOARD: {
			doBeforeSingleAction();
			sendToServer(REG);
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
* privates*
***********/

void Controller::searhDevices() {	
	do {
		if (_rfManager->searchDevices()) {
			_dataBase->saveDevicesIdsToEeprom();
		}
		delay(100);
	} 
	while (!WAS_MENU_BTN_PRESSED);
	
	WAS_MENU_BTN_PRESSED = false;	
}

int Controller::selectMaxInputerTextLen(Action pAction) {
	switch (pAction) {
		case act_SELECT_WIFI: 			return _dataBase->getMaxLenOfSsid();
		case act_INPUT_WIFI_SSID: 		return _dataBase->getMaxLenOfSsid();
		case act_INPUT_WIFI_PSWD: 		return _dataBase->getMaxLenOfSsidPassword();
		case act_INPUT_TCP_LOGIN: 		return _dataBase->getMaxLenOfLogin();
		case act_INPUT_TCP_PSWD: 		return _dataBase->getMaxLenOfPassword();
		case act_INPUT_SERVER_IP: 		return _dataBase->getMaxLenOfHost();
		case act_INPUT_SERVER_PORT: 	return _dataBase->getMaxLenOfPort();		
		case act_INPUT_SERVER_TARGET: 	return _dataBase->getMaxLenOfTarget();
    }
}

void Controller::activateInputer(int pMaxInputerTextLen) {
	_displayEG->notifyDisplayToShowInputer();
	String maxInputerTextLen = String(pMaxInputerTextLen);
	Event* eventWithMaxInputLen = new Event(maxInputerTextLen);
	_inputerEG->notifyInputerToClear(eventWithMaxInputLen);
	delete eventWithMaxInputLen;
	_displayEG->notifyDisplayToShowCurrInputSymbol();
	_mnSelector->setActive(false);
	_inputer->setActive(true);
}

void Controller::activateChooser(String* pWifiNames, int pCount) {
	_displayEG->notifyDisplayToShowChooser();
	_chooser->reinit(pWifiNames, pCount);
	_displayEG->notifyDisplayToShowCurrChooserElement();
	_mnSelector->setActive(false);
	_chooser->setActive(true);
	Serial.println(F("HERE Controller::activateChooser end"));
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

void Controller::setRFManager(RFManager* pRfManager) {
	_rfManager = pRfManager;
}

void Controller::setDataBase(DataBase* pDataBase) {
	_dataBase = pDataBase;
}

void Controller::setWifiManager(WifiManager* pWifiManager) {
	Serial.println("Controller::setWifiManager initWifi()");
	_wifiManager = pWifiManager;
	initWifi();
}

void Controller::setMenuToActionMap(HashMap<String, Action, 12/*unsigned int*/> pMenuToActionMap) {
	_menuToActionMap = pMenuToActionMap;
}