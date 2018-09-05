// DeviceEepromManager
// (c) Ivanov Aleksandr, 2018

#include "DeviceEepromManager.h"
#include <EEPROM.h>

DeviceEepromManager::DeviceEepromManager() {
	init();
	Serial.println("DeviceEepromManager()!");//TEST
}

DeviceEepromManager::~DeviceEepromManager() {
}

void EepromManager::init() {
	
	//saveBoardUID(0);// need rewrite by value from server when registration occur
	
	//STUB
	uint8_t deviceId = 1; // 1 because we should have some "from" number for registration connection
	
	saveDevicesId(deviceId);
	
	float deviceCtrl = 0.0;
	float deviceMin = 0.0;
	float deviceMax = 1.0;
	float deviceDiscrete = 1.0;
	
	saveFloat(eepr_deviceCtrls, deviceCtrl);
	saveFloat(eepr_deviceMins, deviceMin);	
	saveFloat(eepr_deviceMaxs, deviceMax);	
	saveFloat(eepr_deviceDiscretes, deviceDiscrete);
	
	bool deviceDigitalBool = true;
	bool deviceAnalogBool = false;
	bool deviceAdjustableBool = true;
	bool deviceRotatableBool = false;	

	saveBool(eepr_deviceDigitalBools, deviceDigitalBool);	
	saveBool(eepr_deviceAnalogBools, deviceAnalogBool);	
	saveBool(eepr_deviceAdjustableBools, deviceAdjustableBool);	
	saveBool(eepr_deviceRotatableBools, deviceRotatableBool);	
}

/******************
* public interface*
*******************/
	// Caution! Do not forget to clean memory before first using of board
	
void DeviceEepromManager::saveBoardUID(long pData) {
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	memoryDtoUnion.memoryDTO._uniqID = pData;
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

bool DeviceEepromManager::saveFloat(EepromPlaceName pName, float pData) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceCtrls: {
			memoryDtoUnion.memoryDTO._deviceCtrl = pData; 
			result = true;
			break;
		}
		case eepr_deviceMins: {
			memoryDtoUnion.memoryDTO._deviceMin = pData; 
			result = true;
			break;
		}
		case eepr_deviceMaxs: {
			memoryDtoUnion.memoryDTO._deviceMax = pData; 
			result = true;
			break;
		}
		case eepr_deviceDiscretes: {
			memoryDtoUnion.memoryDTO._deviceDiscrete = pData; 
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

bool DeviceEepromManager::saveBool(EepromPlaceName pName, bool pFlag) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBools: {
			memoryDtoUnion.memoryDTO._deviceDigitalBool = pFlag; 
			result = true;
			break;
		}		
		case eepr_deviceAnalogBools: {
			memoryDtoUnion.memoryDTO._deviceAnalogBool = pFlag; 
			result = true; 
			break;
		}
		case eepr_deviceAdjustableBools: {
			memoryDtoUnion.memoryDTO._deviceAdjustableBool = pFlag;
			result = true;
			break;
		}
		case eepr_deviceRotatableBools: {
			memoryDtoUnion.memoryDTO._deviceRotatableBool = pFlag;
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

void DeviceEepromManager::saveDeviceId(uint8_t id) {
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	Serial.print(String(F("EepromManager::saveDevicesIds id")) + i + " = ");//TEST 
	Serial.println(pIdsBuffer[i]);//TEST
	memoryDtoUnion.memoryDTO._deviceId = id;
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

float DeviceEepromManager::fetchFloat(EepromPlaceName pName) {
	float result = 0.0;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceCtrl: {
			result = memoryDtoUnion.memoryDTO._deviceCtrl;
			break;
		}
		case eepr_deviceMin: {
			result = memoryDtoUnion.memoryDTO._deviceMin;
			break;
		}
		case eepr_deviceMax: {
			result = memoryDtoUnion.memoryDTO._deviceMax;
			break;
		}
		case eepr_deviceDiscrete: {
			result = memoryDtoUnion.memoryDTO._deviceDiscrete;
			break;
		}
	}
	return result;
}

bool DeviceEepromManager::fetchBool(EepromPlaceName pName) {
	bool result = false; // TODO it is not right to return false in wrong case. False is also a result
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBools: {
			result = memoryDtoUnion.memoryDTO._deviceDigitalBool;
			break;
		}		
		case eepr_deviceAnalogBools: {
			result = memoryDtoUnion.memoryDTO._deviceAnalogBool;
			break;
		}
		case eepr_deviceAdjustableBools: {
			result = memoryDtoUnion.memoryDTO._deviceAdjustableBool;
			break;
		}
		case eepr_deviceRotatableBools: {
			result = memoryDtoUnion.memoryDTO._deviceRotatableBool;
			break;
		}
	}
	return result;
}

long DeviceEepromManager::fetchBoardUID() {
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return memoryDtoUnion.memoryDTO._uniqID;
}