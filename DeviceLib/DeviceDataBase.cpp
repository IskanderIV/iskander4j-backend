// DeviceDataBase
// (c) Ivanov Aleksandr, 2018

#include "DeviceDataBase.h"

DeviceDataBase::DeviceDataBase() {
	init();
}

DeviceDataBase::~DeviceDataBase() {
}

void DeviceDataBase::init() {
	_eepromMngr = new DeviceEepromManager();
	_deviceAck = 0.0;
	_radioError = false;
	
	if (_eepromMngr) {
		initFromEeprom();
	}
}

void DeviceDataBase::initFromEeprom() {
	// boardUID
	_boardUID = _eepromMngr->fetchBoardUID();
	
	// id
	_deviceId = _eepromMngr->fetchFloat(eepr_deviceMin);
	
	// min
	_min = _eepromMngr->fetchFloat(eepr_deviceMin);

	// max
	_max = _eepromMngr->fetchFloat(eepr_deviceMax);

	// discrete
	_discrete = _eepromMngr->fetchFloat(eepr_deviceDiscrete);

	// deviceCtrl
	_controlValue = _eepromMngr->fetchFloat(eepr_deviceCtrl);

	// deviceDigital
	_digital = _eepromMngr->fetchBool(eepr_deviceDigitalBool);

	// deviceAnalog
	_analog = _eepromMngr->fetchBool(eepr_deviceAnalogBool);

	// deviceAdjustable
	_adjustable = _eepromMngr->fetchBool(eepr_deviceAdjustableBool);

	// deviceRotatable
	_rotatable = _eepromMngr->fetchBool(eepr_deviceRotatableBool);

}

/******************
* Public interface*
*******************/

long DeviceDataBase::getBoardUID() {
	return _boardUID;
}

void DeviceDataBase::setBoardUID(long pBoardUID) {
	if (getBoardUID() != pBoardUID) {
		_eepromMngr->saveBoardUID(pBoardUID);
	}
	_boardUID = pBoardUID;
}

uint8_t DeviceDataBase::getDeviceId() {
	return _deviceId;
}

void DeviceDataBase::setDeviceId(uint8_t pDeviceId) {
	if (getDeviceId() != pDeviceId) {
		_eepromMngr->saveDeviceId(pDeviceId);
	}
	_deviceId = pDeviceId;
}

float DeviceDataBase::getDeviceAck() {
	return _deviceAck;
}

void DeviceDataBase::setDeviceAck(float pAck) {
	_deviceAck = pAck;
}

float DeviceDataBase::getDeviceMin() {
	return _min;
}

void DeviceDataBase::setDeviceMin(float pMin) {
	if (getDeviceMin() != pMin) {
		_eepromMngr->saveFloat(eepr_deviceMin, pMin);
	}
	_min = pMin;
}

float DeviceDataBase::getDeviceMax() {
	return _max;
}

void DeviceDataBase::setDeviceMax(float pMax) {
	if (getDeviceMax() != pMax) {
		_eepromMngr->saveFloat(eepr_deviceMax, pMax);
	}
	_max = pMax;
}

float DeviceDataBase::getDeviceDiscrete() {
	return _discrete;
}

void DeviceDataBase::setDeviceDiscrete(float pDiscrete) {
	if (getDeviceMax() != pDiscrete) {
		_eepromMngr->saveFloat(eepr_deviceDiscrete, pDiscrete);
	}
	_discrete = pDiscrete;
}

float DeviceDataBase::getDeviceControlValue() {
	return _controlValue;
}

void DeviceDataBase::setDeviceControlValue(float pControlValue) {
	if (getDeviceControlValue() != pControlValue) {
		_eepromMngr->saveFloat(eepr_deviceCtrl, pControlValue);
	}
	_controlValue = pControlValue;
}

bool DeviceDataBase::getDeviceDigital() {
	return _digital;
}

void DeviceDataBase::setDeviceDigital(bool pDigital) {
	if (getDeviceDigital() != pDigital) {
		_eepromMngr->saveBool(eepr_deviceDigitalBool, pDigital);
	}
	_digital = pDigital;
}

bool DeviceDataBase::getDeviceAnalog() {
	return _analog;
}

void DeviceDataBase::setDeviceAnalog(bool pAnalog) {
	if (getDeviceAnalog() != pAnalog) {
		_eepromMngr->saveBool(eepr_deviceAnalogBool, pAnalog);
	}
	_analog = pAnalog;
}

bool DeviceDataBase::getDeviceAdj() {
	return _adjustable;
}

void DeviceDataBase::setDeviceAdj(bool pAdj) {
	if (getDeviceAdj() != pAdj) {
		_eepromMngr->saveBool(eepr_deviceAdjustableBool, pAdj);
	}
	_adjustable = pAdj;
}

bool DeviceDataBase::getDeviceRotatable() {
	return _rotatable;
}

void DeviceDataBase::setDeviceRotatable(bool pRotatable) {
	if (getDeviceRotatable() != pRotatable) {
		_eepromMngr->saveBool(eepr_deviceRotatableBool, pRotatable);
	}
	_rotatable = pRotatable;
}

bool DeviceDataBase::getDeviceRFErr() {
	return _radioError;
}

void DeviceDataBase::setDeviceRFErr(bool pRadioError) {
	_radioError = pRadioError;
}

/*********************
*	Private interface*
**********************/
