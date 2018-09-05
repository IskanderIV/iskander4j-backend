// DataBase
// (c) Ivanov Aleksandr, 2018

#include "DataBase.h"
#include "EepromManager.h"

DataBase::DataBase(EepromManager* pEepromMngr) {	
	init(pEepromMngr);
}

DataBase::~DataBase() {
	if (_deviceJsonList) {		// TODO Delete every JSON Element
		for(int i = 0; i < _deviceCount; i++) {
			// dont need. Data base dead when device is switched off
		}
	}
}

void DataBase::init(EepromManager* pEepromMngr) {
	_eepromMngr = pEepromMngr;
	
	// init device list
	_deviceJsonList = nullptr;
	_lastDeviceJson = nullptr;
	_deviceCount = 0;
	
	initFromEeprom();
	
	//init global flags
	_gsmError = false;
	_radioError = false;
	_lcdError = false;
}

void DataBase::initFromEeprom() {
	if (_eepromMngr) {
		_maxDevices = _eepromMngr->getMaxByteOfPlace(eepr_deviceIds);
		_maxLenOfSsid = _eepromMngr->getMaxByteOfPlace(eepr_wifiLogin);
		_maxLenOfSsidPassword = _eepromMngr->getMaxByteOfPlace(eepr_wifiPsswd);
		_maxLenOfLogin = _eepromMngr->getMaxByteOfPlace(eepr_tcpLogin);
		_maxLenOfPassword = _eepromMngr->getMaxByteOfPlace(eepr_tcpPsswd);
		_maxLenOfHost = _eepromMngr->getMaxByteOfPlace(eepr_serverAdress);
		_maxLenOfPort = _eepromMngr->getMaxByteOfPlace(eepr_serverPort);
		_maxLenOfTarget = _eepromMngr->getMaxByteOfPlace(eepr_target);
		_maxLenOfBoardUidSymbols = _eepromMngr->getMaxByteOfPlace(eepr_baseId);
		
		// init boardUID
		_uniqBaseID = _eepromMngr->fetchBoardUID();
		
		// init strings
		_SSID = _eepromMngr->fetchString(eepr_wifiLogin);
		_ssidPassword = _eepromMngr->fetchString(eepr_wifiPsswd);
		
		_login = _eepromMngr->fetchString(eepr_tcpLogin);
		_password = _eepromMngr->fetchString(eepr_tcpPsswd);
		
		_host = _eepromMngr->fetchString(eepr_serverAdress);
		_port = _eepromMngr->fetchString(eepr_serverPort);
		_target = _eepromMngr->fetchString(eepr_target);
		
		// init devices
		uint8_t idsBuffer[_maxDevices];
		memset(idsBuffer, 0, _maxDevices);
		_eepromMngr->fetchIds(idsBuffer);
		for (uint8_t i = 0; i < _maxDevices; i++) {
			if (idsBuffer[i] != 0) {
				uint8_t id = idsBuffer[i];
				addDeviceInfo(id);
				fillDeviceInfoFromEeprom(id);
			}
		}
	}
}

void DataBase::fillDeviceInfoFromEeprom(uint8_t id) {
	DeviceInfo* currDevice = findDeviceInfo(id);
	// min
	float min = _eepromMngr->fetchDeviceFloat(eepr_deviceMins, id);
	currDevice->setMin(min);
	// max
	float max = _eepromMngr->fetchDeviceFloat(eepr_deviceMaxs, id);
	currDevice->setMax(max);
	// discrete
	float discrete = _eepromMngr->fetchDeviceFloat(eepr_deviceDiscretes, id);
	currDevice->setDiscrete(discrete);
	// deviceCtrl
	float deviceCtrl = _eepromMngr->fetchDeviceFloat(eepr_deviceCtrls, id);
	currDevice->setControlVal(deviceCtrl);
	// deviceDigital
	float deviceDigital = _eepromMngr->fetchDeviceBool(eepr_deviceDigitalBools, id);
	currDevice->setDigital(deviceDigital);
	// deviceAnalog
	float deviceAnalog = _eepromMngr->fetchDeviceBool(eepr_deviceAnalogBools, id);
	currDevice->setAnalog(deviceAnalog);
	// deviceAdjustable
	float deviceAdjustable = _eepromMngr->fetchDeviceBool(eepr_deviceAdjustableBools, id);
	currDevice->setAdjustable(deviceAdjustable);
	// deviceRotatable
	float deviceRotatable = _eepromMngr->fetchDeviceBool(eepr_deviceRotatableBools, id);
	currDevice->setRotatable(deviceRotatable);
}

/******************
* Public interface*
*******************/

uint8_t  DataBase::generateId() {
	return getDeviceCount();
}
	
void DataBase::addDeviceInfo(uint8_t id) {	
	if (_deviceCount >= _maxDevices) return false;
	DeviceInfo* added = new DeviceInfo(id);
	// Serial.println(String("DataBase::addDeviceInfo") + (uint8_t)id);//TEST
	if (!_deviceJsonList) {		
		addDeviceFirst(added);
		_deviceCount++;
		// Serial.println("_deviceJsonList is void");//TEST
	} else if (!findDeviceInfo(id)) {
		addDeviceLast(added);
		_deviceCount++;
		// Serial.println("added to _deviceJsonList");//TEST
	}
}

void DataBase::removeDeviceInfo(uint8_t id) {
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

bool DataBase::isDeviceExist(uint8_t id) {
	if (findDeviceInfo(id)) {
		return true;
	} else {
		return false;
	}
}

/*********FLOATS************/

float DataBase::getDeviceAck(uint8_t id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getAck();
	} else {
		return -100.0;
	}
}

void DataBase::setDeviceAck(uint8_t id, float pAck) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		searched->setAck(pAck);
	}
}

float DataBase::getDeviceMin(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getMin();
	} else {
		return -100.0;
	}
}
// use it only with structure requests
void  DataBase::setDeviceMin(uint8_t id, float _min){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getMin() != _min) {
			_eepromMngr->saveFloat(eepr_deviceMins, id, _min);
		}
		searched->setMin(_min);
	}
}

float DataBase::getDeviceMax(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getMax();
	} else {
		return -100.0;
	}
}
// use it only with structure requests
void  DataBase::setDeviceMax(uint8_t id, float _max){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getMax() != _max) {
			_eepromMngr->saveFloat(eepr_deviceMaxs, id, _max);
		}
		searched->setMax(_max);
	}
}

float DataBase::getDeviceDiscrete(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getDiscrete();
	} else {
		return -100.0;
	}
}
// use it only with structure requests
void  DataBase::setDeviceDiscrete(uint8_t id, float _discrete){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getDiscrete() != _discrete) {
			_eepromMngr->saveFloat(eepr_deviceDiscretes, id, _discrete);
		}
		searched->setDiscrete(_discrete);
	}
}

float DataBase::getDeviceControlValue(uint8_t id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getControlVal();
	} else {
		return 0.0;
	}
}

void DataBase::setDeviceControlValue(uint8_t id, float pControl) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getControlVal() != pControl) {
			_eepromMngr->saveFloat(eepr_deviceCtrls, id, pControl);
		}
		searched->setControlVal(pControl);
	}
}

/*********BOOLS************/

bool  DataBase::getDeviceDigital(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getDigital();
	} else {
		return false;
	}
}
// use it only with structure requests
void  DataBase::setDeviceDigital(uint8_t id, bool _digital){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getDigital() != _digital) {
			_eepromMngr->saveBool(eepr_deviceDigitalBools, id, _digital);
		}
		searched->setDigital(_digital);
	}
}

bool  DataBase::getDeviceAnalog(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getAnalog();
	} else {
		return false;
	}
}
// use it only with structure requests
void  DataBase::setDeviceAnalog(uint8_t id, bool _analog){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getAnalog() != _analog) {
			_eepromMngr->saveBool(eepr_deviceAnalogBools, id, _analog);
		}
		searched->setAnalog(_analog);
	}
}

bool DataBase::getDeviceAdj(uint8_t id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getAdjustable();
	} else {
		return false;
	}
}

void DataBase::setDeviceAdj(uint8_t id, bool pAdj) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getAdjustable() != pAdj) {
			_eepromMngr->saveBool(eepr_deviceAdjustableBools, id, pAdj);
		}
		searched->setAdjustable(pAdj);
	}
}

bool  DataBase::getDeviceRotatable(uint8_t id){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->getRotatable();
	} else {
		return false;
	}
}
// use it only with structure requests
void  DataBase::setDeviceRotatable(uint8_t id, bool _rotatable){
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		if (searched->getRotatable() != _rotatable) {
			_eepromMngr->saveBool(eepr_deviceRotatableBools, id, _rotatable);
		}
		searched->setRotatable(_rotatable);
	}
}

bool DataBase::getDeviceRFErr(uint8_t id) {
	DeviceInfo* searched = findDeviceInfo(id);
	if (searched) {
		return searched->hasRadioError();
	} else {
		return false;
	}
}

void DataBase::setDeviceRFErr(uint8_t id, bool pRadioError) {
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
	// Serial.println("DataBase::saveDevicesIdsToEeprom() end");//TEST
}


// WIFI
void DataBase::setSSID(String pSSID) {
	if (strcmp(getSSID().c_str(), pSSID.c_str()) != 0) {
		_eepromMngr->saveString(eepr_wifiLogin, pSSID);
	}
	_SSID = pSSID;
}

String DataBase::getSSID() {
	return _SSID;
}

void DataBase::setSsidPassword(String pSsidPassword) {
	if (strcmp(getSsidPassword().c_str(), pSsidPassword.c_str()) != 0) {
		_eepromMngr->saveString(eepr_wifiPsswd, pSsidPassword);
	}
	_ssidPassword = pSsidPassword;
}

String DataBase::getSsidPassword() {
	return _ssidPassword;
}

// TCP
void DataBase::setLogin(String pLogin) {
	if (strcmp(getLogin().c_str(), pLogin.c_str()) != 0) {
		_eepromMngr->saveString(eepr_tcpLogin, pLogin);
	}
	_login = pLogin;
}
String DataBase::getLogin() {
	return _login;
}
void DataBase::setPassword(String pPassword) {
	if (strcmp(getPassword().c_str(), pPassword.c_str()) != 0) {
		_eepromMngr->saveString(eepr_tcpPsswd, pPassword);
	}
	_password = pPassword;
}
String DataBase::getPassword() {
	return _password;
}

// Site
void DataBase::setHost(String pHost) {
	if (strcmp(getHost().c_str(), pHost.c_str()) != 0) {
		_eepromMngr->saveString(eepr_serverAdress, pHost);
	}
	_host = pHost;
}

String DataBase::getHost() {
	return _host;
}

void DataBase::setPort(String pPort) {
	if (strcmp(getPort().c_str(), pPort.c_str()) != 0) {
		_eepromMngr->saveString(eepr_serverPort, pPort);
	}
	_port = pPort;
}

String DataBase::getPort() {
	return _port;
}

void DataBase::setTarget(String pTarget) {
	if (strcmp(getTarget().c_str(), pTarget.c_str()) != 0) {
		_eepromMngr->saveString(eepr_target, pTarget);
	}
	_target = pTarget;
}

String DataBase::getTarget() {
	return _target;
}

// MAX LENGTHS
int DataBase::getMaxLenOfSsid() {
	return _maxLenOfSsid;
}

int DataBase::getMaxLenOfSsidPassword() {
	return _maxLenOfSsidPassword;
}

int DataBase::getMaxLenOfLogin() {
	return _maxLenOfLogin;
}

int DataBase::getMaxLenOfPassword() {
	return _maxLenOfPassword;
}

int DataBase::getMaxLenOfHost() {
	return _maxLenOfHost;
}

int DataBase::getMaxLenOfPort() {
	return _maxLenOfPort;
}

int DataBase::getMaxLenOfTarget() {
	return _maxLenOfTarget;
}

int DataBase::getMaxDevices() {
	return _maxDevices;
}

int DataBase::getMaxLenOfBoardUidSymbols() {
	return _maxLenOfBoardUidSymbols;
}
// END MAX LENGTHS

long DataBase::getUniqBaseID() {
	return _uniqBaseID;
}

void DataBase::setUniqBaseID(long pUniqBaseID) {
	if (getUniqBaseID() != pUniqBaseID) {
		_eepromMngr->saveBoardUID(pUniqBaseID);
	}
	_uniqBaseID = pUniqBaseID;
}

// global errors

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

/*********************
*	Private interface*
**********************/



DataBase::DeviceInfo* DataBase::findDeviceInfo(uint8_t id) {
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

DataBase::DeviceInfo::DeviceInfo() {
	_deviceId = 0;
	_deviceAck = 0.0;
	_min = 0.0;
	_max = 1.0;
	_discrete = 1.0;
	_controlValue = 0.0;
	_digital = false;	
	_analog = false;	
	_adjustable = false;	
	_rotatable = false;
	_radioError = false;
	_next = nullptr;
	_prev = nullptr;
}

DataBase::DeviceInfo::DeviceInfo(uint8_t id) {
	_deviceId = id;
	_deviceAck = 0.0;
	_min = 0.0;
	_max = 1.0;
	_discrete = 1.0;
	_controlValue = 0.0;
	_digital = false;	
	_analog = false;	
	_adjustable = false;	
	_rotatable = false;
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

uint8_t DataBase::DeviceInfo::getId() {
	return _deviceId;	
}

void DataBase::DeviceInfo::setId(uint8_t pDeviceId) {
	_deviceId = pDeviceId;
}

float DataBase::DeviceInfo::getAck() {
	return _deviceAck;	
}

void DataBase::DeviceInfo::setAck(float pDeviceAck) {
	_deviceAck = pDeviceAck;
}

float DataBase::DeviceInfo::getMin() {
	return _min;	
}

void DataBase::DeviceInfo::setMin(float pMin) {
	_min = pMin;
}

float DataBase::DeviceInfo::getMax() {
	return _max;	
}

void DataBase::DeviceInfo::setMax(float pMax) {
	_max = pMax;
}

float DataBase::DeviceInfo::getDiscrete() {
	return _discrete;	
}

void DataBase::DeviceInfo::setDiscrete(float pDiscrete) {
	_discrete = pDiscrete;
}

float DataBase::DeviceInfo::getControlVal() {
	return _controlValue;	
}

void DataBase::DeviceInfo::setControlVal(float pControlValue) {
	_controlValue = pControlValue;
}

bool DataBase::DeviceInfo::getDigital() {
	return _digital;	
}

void DataBase::DeviceInfo::setDigital(bool pDigital) {
	_digital = pDigital;
}

bool DataBase::DeviceInfo::getAnalog() {
	return _analog;	
}

void DataBase::DeviceInfo::setAnalog(bool pAnalog) {
	_analog = pAnalog;
}

bool DataBase::DeviceInfo::getAdjustable() {
	return _adjustable;	
}

void DataBase::DeviceInfo::setAdjustable(bool pAdjustable) {
	_adjustable = pAdjustable;
}

bool DataBase::DeviceInfo::getRotatable() {
	return _rotatable;	
}

void DataBase::DeviceInfo::setRotatable(bool pRotatable) {
	_rotatable = pRotatable;
}

bool DataBase::DeviceInfo::hasRadioError() {
	return _radioError;	
}

void DataBase::DeviceInfo::setRadioError(bool pRadioError) {
	_radioError = pRadioError;
}
