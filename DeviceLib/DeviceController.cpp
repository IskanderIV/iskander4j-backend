// DeviceController.cpp
// (c) Ivanov Aleksandr, 2018

#include "DeviceController.h"

DeviceController::DeviceController(DeviceButtonsManager* pBtnManager) : _btnManager(pBtnManager) {	
	init();
	Serial.println("DeviceController()!");//TEST
}

DeviceController::~DeviceController(){
	// TODO
}

void DeviceController::init() {
	_dataBase = new DeviceDataBase();
	_rfManager = new DeviceRFManager(_dataBase);
	_actuator = new DeviceDigitalActuator(_dataBase);
	_sensor = new DeviceSensor(_dataBase);
	_signalisator = new Signalisator(_dataBase);
	
	_workState = false;
	_searchState = false;
	// TODO make control of bool states of device through control of their state when interruption occure
}

/*****************
* public methods *
******************/

void DeviceController::processLoop() {
	processButtons();
	if (_workState) {		
		Serial.println("Work state!");
		doWork();
	} else if (_searchState) {
		Serial.println("Identifying state!");
		doIdentify();
	}
	// else {
		// Serial.println("Else state!");
		// doWork();
	// }	
}

/******************
* private methods *
*******************/

void DeviceController::processButtons() {
	ButtonPin* buttonsStateList = _btnManager->processButtons();
	for (uint8_t i = 0; i < _btnManager->getButtonsCount(); i++) {		
		switch (buttonsStateList[i]) {
			case btn_ONN: {
				Serial.println("Button btn_ONN = pushed");
				_workState = true;
				_searchState = false;
				break;
			}
			case btn_SEARCH: {
				Serial.println("Button btn_SEARCH = pushed");
				if (!_workState) {
					_searchState = true;
				}
				break;
			}
		}
	}
}

void DeviceController::doWork() {
	_rfManager->sendInfo();
	_actuator->process();
	_signalisator->process(ls_BLINK_RARE);
}

void DeviceController::doIdentify() {
	if(_rfManager->identifyDevice()) {
		_signalisator->process(ls_BLINK_OFTEN);
		delay(500);
		_signalisator->process(ls_OFF);
	}
}