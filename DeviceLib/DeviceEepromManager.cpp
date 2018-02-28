// DeviceEepromManager
// (c) Ivanov Aleksandr, 2018

#include "DeviceEepromManager.h"
#include <EEPROM.h>

DeviceEepromManager::DeviceEepromManager() {
	Serial.println("DeviceEepromManager()!");//TEST
}

DeviceEepromManager::~DeviceEepromManager() {
}

/* 
* interface impl for controllers requests 
*
*/
	// TODO Clean memory place before put
void DeviceEepromManager::save(EepromPlaceName pName, long pData) {
	// Serial.print("DeviceEepromManager::save raw data string>> "); Serial.println(pData);
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	switch (pName) {
		case eepr_deviceId: 	dto._deviceId = (uint8_t) pData; break;
		case eepr_baseId: 		dto._uniqID = pData; break;
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, dto);
}

long DeviceEepromManager::fetch(EepromPlaceName pName) {
	long element;
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	switch (pName) {
		case eepr_deviceId: 	element = dto._deviceId; 	break;
		case eepr_baseId: 		element = dto._uniqID; 		break;
	}
	return element;	
}

