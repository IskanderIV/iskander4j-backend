// DeviceController
// (c) Ivanov Aleksandr, 2018

#include "DeviceController.h"
#include "DeviceRFManager.h"
#include "DeviceDataBase.h"
#include "ActuatorInterface.h"
#include "DeviceSensor.h"
#include "Signalisator.h"

DeviceController::DeviceController() {	
	init();
	Serial.println("DeviceController()!");//TEST
}

DeviceController::~DeviceController(){
	// TODO
}

void DeviceController::processLoop() {
	if (!_dataBase) return;
	if (!_rfManager) return;
	if (!_eepromManager) return;
	if (!_btnManager) return; 
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

void DeviceController::doWork() {
	_rfManager->sendInfo();
	if (_dataBase->getDeviceControlValue() > 0.5) {
		_actuator->riseUp();
	} else {
		_actuator->fallDawn();
	}	
	_dataBase->setDeviceAck(_sensor->measure());	
}

void DeviceController::doIdentify() {
	if(_rfManager->identifyDevice()) {
		_signalisator->switchOn();
		_dataBase->setUniqBaseID(_rfManager->getUniqID());
		_dataBase->setDeviceID(_rfManager->getDeviceID());
		_eepromManager->save(eepr_baseId, _dataBase->getUniqBaseID());
		_eepromManager->save(eepr_deviceId, _dataBase->getDeviceID());
	}
}

/**********
* privates
***********/

void DeviceController::init() {	
	_btnManager = NULL;
	_eepromManager = NULL;
	_rfManager = NULL;
	_dataBase = NULL;
	_workState = false;
	_searchState = false;
}

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
			case btn_CONTROLABLE: {
				Serial.println("Button btn_CONTROLABLE = pushed");
				_dataBase->setDeviceAdj(true);
				_dataBase->setDeviceRot(false);//TEST
				break;
			}
		}
	}
}

/*
 Getters and setters
*/

void DeviceController::setBtnManager(DeviceButtonsManager* pBtnManager) {
	_btnManager = pBtnManager;
}

void DeviceController::setEepromManager(DeviceEepromManager* pEepromManager) {
	_eepromManager = pEepromManager;
	_eepromManager->save(eepr_baseId, 1010101L);//TEST
	//_eepromManager->save(eepr_deviceId, 2);//MINI2
	_eepromManager->save(eepr_deviceId, 1);//TEST
}

void DeviceController::setRFManager(DeviceRFManager* pRfManager) {
	_rfManager = pRfManager;
}

void DeviceController::setDataBase(DeviceDataBase* pDataBase) {
	_dataBase = pDataBase;
}

void DeviceController::setActuator(ActuatorInterface* pActuator) {
	_actuator = pActuator;
}

void DeviceController::setDeviceSensor(DeviceSensor* pDeviceSensor) {
	_sensor = pDeviceSensor;
}

void DeviceController::setSignalisator(Signalisator* pSignalisator) {
	_signalisator = pSignalisator;
}