// DataBase
// (c) Ivanov Aleksandr, 2018

#include "DataBase.h"
#include "EepromManager.h"

DataBase::DataBase() {	
	_deviceJsonList = nullptr;
	_lastDeviceJson = nullptr;
	_eepromMngr = nullptr;
	_deviceCount = 0;
	_gsmError = false;
	_radioError = false;
	_lcdError = false;
}

DataBase::~DataBase() {
	if (_deviceJsonList) {		// TODO Delete every JSON Element
		for(int i = 0; i < _deviceCount; i++) {
			
		}
	}
}

/*
*	Public interface
*/
	
void DataBase::addDeviceInfo(char id) {
	
	if (_deviceCount >= _maxDevices) return;
	DeviceInfo* added = new DeviceInfo(id);
	Serial.println(String("DataBase::addDeviceInfo") + (uint8_t)id);//TEST
	if (!_deviceJsonList) {		
		addDeviceFirst(added);
		_deviceCount++;
		Serial.println("_deviceJsonList is void");//TEST
	} else if (!findDeviceInfo(id)) {
		Serial.println("_deviceJsonList is NOT void");//TEST
			// DeviceInfo* curr = _deviceJsonList;
			// bool wasAdded = false;
			// while (curr->getNext()) {
				// if (curr->getId() > id) {
					// DeviceInfo* prev = curr->getPrev();
					// added = new DeviceInfo(id);
					// added->setPrev(prev);
					// added->setNext(curr);
					// prev->setNext(added);
					// curr->setPrev(added);
					// wasAdded = true;
					// break;
				// }
				// curr = curr->getNext();
			// }
			// if (!wasAdded) {
				// addDeviceLast(added);
			// }
		addDeviceLast(added);
		_deviceCount++;
		Serial.println("added to _deviceJsonList");//TEST
	}	
}

void DataBase::removeDeviceInfo(char id) {
	DeviceInfo* removed = findDeviceInfo(id);
	if (removed) {
		DeviceInfo* prev = removed->getPrev();
		DeviceInfo* next = removed->getNext();
		if (prev && next) {
			prev->setNext(next);
			next->setPrev(prev);
		} else if (!prev && next) {
			_deviceJsonList = next;
			next->setPrev(nullptr);
		} else if (prev && !next) {
			_lastDeviceJson = prev;
			prev->setNext(nullptr);
		} else if (!prev && !next) {
			_deviceJsonList = nullptr;
			_lastDeviceJson = nullptr;
		}
		removed->~DeviceInfo();
	}
}

bool DataBase::isDeviceExist(char id) {
	if (findDeviceInfo(id)) {
		return true;
	} else {
		return false;
	}
}

float DataBase::getDeviceAck(char id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getAck();
	} else {
		return 0.0;
	}
}

void DataBase::setDeviceAck(char id, float pAck) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		searched->setAck(pAck);
	}
}

bool DataBase::getDeviceAdj(char id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getAdjustable();
	} else {
		return false;
	}
}

void DataBase::setDeviceAdj(char id, bool pAdj) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		searched->setAdjustable(pAdj);
	}
}

float DataBase::getDeviceControlValue(char id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getControlVal();
	} else {
		return 0.0;
	}
}

void DataBase::setDeviceControlValue(char id, float pControl) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		searched->setControlVal(pControl);
	}
}

bool DataBase::getDeviceRFErr(char id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->hasRadioError();
	} else {
		return false;
	}
}

void DataBase::setDeviceRFErr(char id, bool pRadioError) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		searched->setRadioError(pRadioError);
	}
}

void DataBase::fetchIds(uint8_t* idsBuffer) {
	DeviceInfo* temp = _deviceJsonList;
	uint8_t counter = 0;
	while (temp) {
		idsBuffer[counter++] = temp->getId();
		temp = temp->getNext();
	}
}

void DataBase::saveDevicesIdsToEeprom() {
	//TODO replace to controller
	uint8_t idsBuffer[_maxDevices];
	memset(idsBuffer, 0, _maxDevices);
	fetchIds(idsBuffer);
	_eepromMngr->saveDevicesIds(idsBuffer);
	Serial.println("DataBase::saveDevicesIdsToEeprom() end");//TEST
}


void DataBase::setEepromManager(EepromManager* pEepromMngr) {
	_eepromMngr = pEepromMngr;
	initFromEeprom();
}

long DataBase::getUniqBaseID() {
	return _uniqBaseID;
}

void DataBase::setUniqBaseID(long pUniqBaseID) {
	_uniqBaseID = pUniqBaseID;
}

bool DataBase::getGsmError() {
	return _gsmError;
}

void DataBase::setGsmError(bool pGsmError) {
	_gsmError = pGsmError;
}

bool DataBase::getRadioError() {
	return _radioError;
}

void DataBase::setRadioError(bool pRadioError) {
	_radioError = pRadioError;
}

bool DataBase::getLcdError() {
	return _lcdError;
}

void DataBase::setLcdError(bool pLcdError) {
	_lcdError = pLcdError;
}


int DataBase::getDeviceCount() {
	//Serial.println(F("Device count in DataBase = ")); Serial.print(_deviceCount);
	return _deviceCount;
}

/*
*	Private interface
*/

void DataBase::initFromEeprom() {
	if (_eepromMngr) {
		_maxDevices = _eepromMngr->getMaxByteOfPlace(eepr_deviceIds);
		_uniqBaseID = (_eepromMngr->fetch(eepr_baseId)).toInt();
		uint8_t idsBuffer[_maxDevices];
		memset(idsBuffer, 0, _maxDevices);
		_eepromMngr->fetchIds(idsBuffer);
		for (uint8_t i = 0; i < _maxDevices; i++) {
			if (idsBuffer[i] != 0) {
				addDeviceInfo((char)idsBuffer[i]);	
			}
		}
	}
}

DataBase::DeviceInfo* DataBase::findDeviceInfo(char id) {
	if (!_deviceJsonList) return nullptr;
	DeviceInfo* temp = _deviceJsonList;
	while (temp) {
		if (temp->getId() == id) return temp;
		temp = temp->getNext();
	}
	return nullptr;
}

void DataBase::addDeviceFirst(DeviceInfo* added) {
	if (_deviceJsonList) {
		added->setNext(_deviceJsonList);
		_deviceJsonList->setPrev(added);		
	} else {
		_lastDeviceJson = added;
	}
	_deviceJsonList = added;	
		
}

void DataBase::addDeviceLast(DeviceInfo* added) {
	if (_lastDeviceJson) {
		added->setPrev(_lastDeviceJson);
		_lastDeviceJson->setNext(added);		
	} else {
		_deviceJsonList = added;
	}
	_lastDeviceJson = added;	
}

/*
*	inner DeviceInfo class
*/

DataBase::DeviceInfo::DeviceInfo(char id) {
	_deviceId = id;
	_deviceAck = 0.0;
	_adjustable = false;
	_controlValue = 0.0;
	_radioError = false;
	_next = nullptr;
	_prev = nullptr;
}

DataBase::DeviceInfo::~DeviceInfo() {	
}

DataBase::DeviceInfo* DataBase::DeviceInfo::getPrev() {
	return _prev;	
}

void DataBase::DeviceInfo::setPrev(DeviceInfo* prev) {
	_prev = prev;
}

DataBase::DeviceInfo* DataBase::DeviceInfo::getNext() {
	return _next;	
}

void DataBase::DeviceInfo::setNext(DeviceInfo* next) {
	_next = next;
}

char DataBase::DeviceInfo::getId() {
	return _deviceId;	
}

void DataBase::DeviceInfo::setId(char pDeviceId) {
	_deviceId = pDeviceId;
}

float DataBase::DeviceInfo::getAck() {
	return _deviceAck;	
}

void DataBase::DeviceInfo::setAck(float pDeviceAck) {
	_deviceAck = pDeviceAck;
}

bool DataBase::DeviceInfo::getAdjustable() {
	return _adjustable;	
}

void DataBase::DeviceInfo::setAdjustable(bool pAdjustable) {
	_adjustable = pAdjustable;
}

float DataBase::DeviceInfo::getControlVal() {
	return _controlValue;	
}

void DataBase::DeviceInfo::setControlVal(float pControlValue) {
	_controlValue = pControlValue;
}

bool DataBase::DeviceInfo::hasRadioError() {
	return _radioError;	
}

void DataBase::DeviceInfo::setRadioError(bool pRadioError) {
	_radioError = pRadioError;
}
