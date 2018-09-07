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

void DeviceEepromManager::init() {
	initMemoryDto();
	//saveBoardUID(0);// need rewrite by value from server when registration occur
	
	//STUB EEPROM DATA
	uint8_t deviceId = 1; // 1 because we should have some "from" number for registration connection
	
	float deviceCtrl = 0.0;
	float deviceMin = 0.0;
	float deviceMax = 1.0;
	float deviceDiscrete = 1.0;
	
	// saveFloat(eepr_deviceCtrl, deviceCtrl);
	// saveFloat(eepr_deviceMin, deviceMin);	
	// saveFloat(eepr_deviceMax, deviceMax);	
	// saveFloat(eepr_deviceDiscrete, deviceDiscrete);
	
	uint8_t deviceDigitalBool = 1;
	uint8_t deviceAnalogBool = 0;
	uint8_t deviceAdjustableBool = 1;
	uint8_t deviceRotatableBool = 0;	

	// saveBool(eepr_deviceDigitalBool, deviceDigitalBool);	
	// saveBool(eepr_deviceAnalogBool, deviceAnalogBool);	
	// saveBool(eepr_deviceAdjustableBool, deviceAdjustableBool);	
	// saveBool(eepr_deviceRotatableBool, deviceRotatableBool);

	memoryDtoUnion.memoryDTO._deviceId = deviceId;
	memoryDtoUnion.memoryDTO._deviceCtrl = deviceCtrl;
	memoryDtoUnion.memoryDTO._deviceMin = deviceMin;
	memoryDtoUnion.memoryDTO._deviceMax = deviceMax;	
	memoryDtoUnion.memoryDTO._deviceDiscrete = deviceDiscrete;
	memoryDtoUnion.memoryDTO._deviceDigitalBool = deviceDigitalBool;
	memoryDtoUnion.memoryDTO._deviceAnalogBool = deviceAnalogBool;
	memoryDtoUnion.memoryDTO._deviceAdjustableBool = deviceAdjustableBool;
	memoryDtoUnion.memoryDTO._deviceRotatableBool = deviceRotatableBool;
	
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

void DeviceEepromManager::initMemoryDto() {
	memoryDtoUnion.memoryDTO._uniqID = BOARD_UID;
	memoryDtoUnion.memoryDTO._deviceId = 0;
	memoryDtoUnion.memoryDTO._deviceCtrl = 0.0;
	memoryDtoUnion.memoryDTO._deviceMin = 0.0;
	memoryDtoUnion.memoryDTO._deviceMax = 0.0;	
	memoryDtoUnion.memoryDTO._deviceDiscrete = 0.0;
	memoryDtoUnion.memoryDTO._deviceDigitalBool = 0;
	memoryDtoUnion.memoryDTO._deviceAnalogBool = 0;
	memoryDtoUnion.memoryDTO._deviceAdjustableBool = 0;
	memoryDtoUnion.memoryDTO._deviceRotatableBool = 0;
	// Serial.println("Cycle i = " + String(i));
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
		case eepr_deviceCtrl: {
			memoryDtoUnion.memoryDTO._deviceCtrl = pData; 
			result = true;
			break;
		}
		case eepr_deviceMin: {
			memoryDtoUnion.memoryDTO._deviceMin = pData; 
			result = true;
			break;
		}
		case eepr_deviceMax: {
			memoryDtoUnion.memoryDTO._deviceMax = pData; 
			result = true;
			break;
		}
		case eepr_deviceDiscrete: {
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
	uint8_t flag = btoi(pFlag);
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBool: {
			memoryDtoUnion.memoryDTO._deviceDigitalBool = flag; 
			result = true;
			break;
		}		
		case eepr_deviceAnalogBool: {
			memoryDtoUnion.memoryDTO._deviceAnalogBool = flag; 
			result = true; 
			break;
		}
		case eepr_deviceAdjustableBool: {
			memoryDtoUnion.memoryDTO._deviceAdjustableBool = flag;
			result = true;
			break;
		}
		case eepr_deviceRotatableBool: {
			memoryDtoUnion.memoryDTO._deviceRotatableBool = flag;
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

void DeviceEepromManager::saveDeviceId(uint8_t id) {
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
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
		case eepr_deviceDigitalBool: {
			result = memoryDtoUnion.memoryDTO._deviceDigitalBool;
			break;
		}		
		case eepr_deviceAnalogBool: {
			result = memoryDtoUnion.memoryDTO._deviceAnalogBool;
			break;
		}
		case eepr_deviceAdjustableBool: {
			result = memoryDtoUnion.memoryDTO._deviceAdjustableBool;
			break;
		}
		case eepr_deviceRotatableBool: {
			result = memoryDtoUnion.memoryDTO._deviceRotatableBool;
			break;
		}
	}
	return result;
}

long DeviceEepromManager::fetchBoardUID() {
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return memoryDtoUnion.memoryDTO._uniqID;
}

uint8_t DeviceEepromManager::fetchDeviceId() {
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return memoryDtoUnion.memoryDTO._deviceId;
}