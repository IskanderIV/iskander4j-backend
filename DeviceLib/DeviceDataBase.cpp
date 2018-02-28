// DeviceDataBase
// (c) Ivanov Aleksandr, 2018

#include "DeviceDataBase.h"
#include "DeviceEepromManager.h"

DeviceDataBase::DeviceDataBase() {
	_eepromManager = NULL;	
	_uniqBaseID = -100L;
	_deviceId = 1;
	_deviceAck = 0.0;
	_controlValue = 0.0;
	_adjustable = false;	
	_rotatable = false;
	_radioError = false;
}

DeviceDataBase::~DeviceDataBase() {
}

/*
*	Public interface
*/

void DeviceDataBase::setEepromManager(DeviceEepromManager* pEepromManager) {
	_eepromManager = pEepromManager;
	_uniqBaseID = _eepromManager->fetch(eepr_baseId);
	_deviceId = (uint8_t) _eepromManager->fetch(eepr_deviceId);
}

long DeviceDataBase::getUniqBaseID() {
	return _uniqBaseID;
}

void DeviceDataBase::setUniqBaseID(long pUniqBaseID) {
	_uniqBaseID = pUniqBaseID;
}

uint8_t DeviceDataBase::getDeviceID() {
	return _deviceId;
}

void DeviceDataBase::setDeviceID(uint8_t pDeviceID) {
	_deviceId = pDeviceID;
}

float DeviceDataBase::getDeviceAck() {
	return _deviceAck;
}

void DeviceDataBase::setDeviceAck(float pAck) {
	_deviceAck = pAck;
}

float DeviceDataBase::getDeviceControlValue() {
	return _controlValue;
}

void DeviceDataBase::setDeviceControlValue(float pControlValue) {
	_controlValue = pControlValue;
}

bool DeviceDataBase::isDeviceAdj() {
	return _adjustable;
}

void DeviceDataBase::setDeviceAdj(bool pAdj) {
	_adjustable = pAdj;
}

bool DeviceDataBase::isDeviceRot() {
	return _rotatable;
}

void DeviceDataBase::setDeviceRot(bool pRot) {
	_rotatable = pRot;
}

bool DeviceDataBase::isDeviceRFErr() {
	return _radioError;
}

void DeviceDataBase::setDeviceRFErr(bool pRadioError) {
	_radioError = pRadioError;
}

/*
*	Private interface
*/
