// Controller
// (c) Ivanov Aleksandr, 2018

#ifndef _Controller_H_
#define _Controller_H_

#include "SelectorControllerObserver.h"
#include "ButtonsManager.h"
#include "EepromManager.h"
#include "HashMap.h"
#include "GlobalFlags.h"
#include "HttpExchangeType.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

enum Action {
	act_INPUT_WIFI_SSID,
	act_INPUT_WIFI_PSWD,
	act_INPUT_TCP_LOGIN,
	act_INPUT_TCP_PSWD,
	act_INPUT_SERVER_IP,
	act_INPUT_SERVER_PORT,
	act_INPUT_SERVER_TARGET,
	act_RESET_WIFI,
	act_RESET_GSM,
	act_SEARCH_DEVICES,
	act_SELECT_WIFI,
	act_REGISTER_BOARD,
	act_INPUT_BOARD_UID,
	act_CLEAR_MEMORY	
};

//#define DEBUG
// class SelectorControllerObserver;
class MenuSelectorEventGenerator;
class DisplayEventGenerator;
class InputerEventGenerator;
class ControllerSelectorObserver;
class ControllerDisplayObserver;
class ControllerInputerObserver;
class ControllerChooserInterface;
class ButtonsManager;
class EepromManager;
class RFManager;
class WifiManager;
class DataBase;
class Event;
template<typename K, typename V, unsigned int capacity>
class HashMap;

class Controller : public SelectorControllerObserver
{
public:
	Controller();
	~Controller();
	
	void processLoop();
	
	// SelectorControllerObserver interface
	virtual void executeAction();
	virtual void processMenuExit();
	
	//setters
	void setMenuSelector(ControllerSelectorObserver* _mnSelector);
	void setDisplay(ControllerDisplayObserver* _display);
	void setInputer(ControllerInputerObserver* _inputer);
	void setChooser(ControllerChooserInterface* _chooser);
	void setBtnManager(ButtonsManager* _btnManager);
	void setEepromManager(EepromManager* _eepromManager);
	void setRFManager(RFManager* _rfManager);
	void setWifiManager(WifiManager* _wifiManager);
	void setDataBase(DataBase* _dataBase);
	void setMenuToActionMap(HashMap<String, Action, 14/*unsigned int*/> _menuToActionMap);
	
private:
	MenuSelectorEventGenerator* _mnSelectorEG;
	DisplayEventGenerator* 		_displayEG;
	InputerEventGenerator* 		_inputerEG;
	ControllerSelectorObserver* _mnSelector;
	ControllerDisplayObserver* 	_display;
	ControllerInputerObserver* 	_inputer;
	ControllerChooserInterface* _chooser;
	ButtonsManager* 			_btnManager;
	EepromManager* 				_eepromManager;
	WifiManager* 				_wifiManager;
	RFManager* 					_rfManager;
	DataBase* 					_dataBase;
	
	HashMap<String, Action, 14/*unsigned int*/> _menuToActionMap;
	
	bool _isWifiConnectionOk;
	
	void init();
	void initWifi();
	void quizeDevices();
	void sendToServer(HttpExchangeType type);
	void clearMemory();
	void resetWifi();
	void goWithMenuSelector(ButtonPin _pushedBtnCode);
	void goWithInputer(ButtonPin _pushedBtnCode);
	void goWithChooser(ButtonPin _pushedBtnCode);
	void saveInputText(Action pAction, String& pSavedText);
	void activateInputer(int _maxInputerTextLen);
	void activateChooser(String* pWifiNames, int pCount);
	void doBeforeSingleAction();
	void doAfterSingleAction();
	int selectMaxInputerTextLen(Action _action);
	EepromPlaceName mapActionToEepromPlace(Action);
	ButtonPin obtainPushedBtnCode();
	void searhDevices();
};

#endif