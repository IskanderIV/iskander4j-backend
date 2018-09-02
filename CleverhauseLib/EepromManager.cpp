// EepromManager
// (c) Ivanov Aleksandr, 2018

#include "EepromManager.h"
#include <EEPROM.h>

EepromManager::EepromManager() {
	Serial.println("EepromManager()!");//TEST
}

EepromManager::~EepromManager() {
}

void EepromManager::init() {
	
	saveBoardUID(BOARD_UID);// need rewrite by value from server when registration occur
	
	//STUB	
	//String wifiLogin = String("acer Liquid Z630"); 
	String wifiLogin = String("RAZVRAT_HOUSE");
	//String wifiPsswd = String("111222333");
	String wifiPsswd = String("LaserJet");
	saveString(eepr_wifiLogin, wifiLogin);
	saveString(eepr_wifiPsswd, wifiPsswd);
	String tcpServerIP = F("192.168.1.34");
	String tcpServerPort = F("8090");
	String tcpServertarget = F("/cleverhause/boards/board/data");
	saveString(eepr_serverAdress, tcpServerIP);
	saveString(eepr_serverPort, tcpServerPort);
	saveString(eepr_target, tcpServertarget);
	
	// STUB DEVICE DATA replace with new DeviceInfo when unique class will be created
	const int max_devices = (int) MAX_DEVICES;
	uint8_t deviceIds[max_devices] = {1,2,0,0,0,0,0,0};
	saveDevicesIds(deviceIds);
	
	float deviceCtrls[max_devices] = {1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceMins[max_devices] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceMaxs[max_devices] = {1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceDiscretes[max_devices] = {1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0};
	bool deviceDigitalBools[max_devices] = {true,true,false,false,false,false,false,false};
	bool deviceAnalogBools[max_devices] = {false,false,false,false,false,false,false,false};
	bool deviceAdjustableBools[max_devices] = {true,true,false,false,false,false,false,false};
	bool deviceRotatableBools[max_devices] = {false,false,false,false,false,false,false,false};
	
	for (int i = 0; i < max_devices; i++) {
		saveFloat(eepr_deviceCtrls, deviceCtrls[i]);
		saveFloat(eepr_deviceMins, deviceMins[i]);	
		saveFloat(eepr_deviceMaxs, deviceMaxs[i]);	
		saveFloat(eepr_deviceDiscretes, deviceDiscretes[i]);	
		saveBool(eepr_deviceDigitalBools, deviceDigitalBools[i]);	
		saveBool(eepr_deviceAnalogBools, deviceAnalogBools[i]);	
		saveBool(eepr_deviceAdjustableBools, deviceAdjustableBools[i]);	
		saveBool(eepr_deviceRotatableBools, deviceRotatableBools[i]);
	}	
}

/******************
* public interface*
*******************/
	// Caution! Do not forget to clean memory before first using of board

bool EepromManager::saveString(EepromPlaceName pName, String& pData) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_target: 	{
			strncat(memoryDtoUnion.memoryDTO._serverTarget, pData.c_str(), SERVER_TARGET_NUM_BYTES); 
			result = true;
			break;
		}
		case eepr_serverPort: 	{
			strncat(memoryDtoUnion.memoryDTO._serverPort, pData.c_str(), SERVER_PORT_NUM_BYTES); 
			result = true;
			break;
		}
		case eepr_serverAdress:	{
			strncat(memoryDtoUnion.memoryDTO._serverAdress, pData.c_str(), SERVER_ADRESS_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_tcpPsswd: {
			strncat(memoryDtoUnion.memoryDTO._tcpPsswd, pData.c_str(), TCP_PSSWD_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_tcpLogin: {
			strncat(memoryDtoUnion.memoryDTO._tcpLogin, pData.c_str(), TCP_LOGIN_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_wifiPsswd: {
			strncat(memoryDtoUnion.memoryDTO._wifiPsswd, pData.c_str(), WIFI_PSSWD_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_wifiLogin: {
			strncat(memoryDtoUnion.memoryDTO._wifiLogin, pData.c_str(), WIFI_SSID_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_baseId: {
			strncat(memoryDtoUnion.memoryDTO._uniqID, pData.c_str(), BASE_ID_NUM_BYTES); 
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

void EepromManager::saveBoardUID(long pData) {
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	memoryDtoUnion.memoryDTO._uniqID = pData;
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

bool EepromManager::saveFloat(EepromPlaceName pName, uint8_t id, float pData) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceCtrls: {
			memoryDtoUnion.memoryDTO._deviceCtrls[id] = pData; 
			result = true;
			break;
		}
		case eepr_deviceMins: {
			memoryDtoUnion.memoryDTO._deviceMins[id] = pData; 
			result = true;
			break;
		}
		case eepr_deviceMaxs: {
			memoryDtoUnion.memoryDTO._deviceMaxs[id] = pData; 
			result = true;
			break;
		}
		case eepr_deviceDiscretes: {
			memoryDtoUnion.memoryDTO._deviceDiscretes[id] = pData; 
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

bool EepromManager::saveBool(EepromPlaceName pName, uint8_t id, bool pFlag) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBools: {
			memoryDtoUnion.memoryDTO._deviceDigitalBools[id] = pData; 
			result = true;
			break;
		}		
		case eepr_deviceAnalogBools: {
			memoryDtoUnion.memoryDTO._deviceAnalogBools[id] = pData; 
			result = true; 
			break;
		}
		case eepr_deviceAdjustableBools: {
			memoryDtoUnion.memoryDTO._deviceAdjustableBools[id] = pData;
			result = true;
			break;
		}
		case eepr_deviceRotatableBools: {
			memoryDtoUnion.memoryDTO._deviceRotatableBools[id] = pData;
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

void EepromManager::saveId(uint8_t id) {
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	memoryDtoUnion.memoryDTO._deviceIds[id] = id;
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

bool EepromManager::removeId(EepromPlaceName pName, uint8_t id) {
	bool result = false;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_baseId: memoryDtoUnion.memoryDTO._deviceIds[id] = 0;
		result = true;
		break;
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

void EepromManager::saveDevicesIds(uint8_t* pIdsBuffer) {
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		Serial.print(String(F("EepromManager::saveDevicesIds id")) + i + " = ");//TEST 
		Serial.println(pIdsBuffer[i]);//TEST
		dto._deviceIds[i] = pIdsBuffer[i];
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, dto);
}

String EepromManager::fetchString(EepromPlaceName pName) {
	String element = "";
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	switch (pName) {
		case eepr_serverPort: 	element += dto._serverPort; 	break;
		case eepr_serverAdress:	element += dto._serverAdress; 	break;
		case eepr_tcpPsswd: 	element += dto._tcpPsswd; 		break;
		case eepr_tcpLogin: 	element += dto._tcpLogin; 		break;
		case eepr_wifiPsswd: 	element += dto._wifiPsswd; 		break;
		case eepr_wifiLogin: 	element += dto._wifiLogin; 		break;
		case eepr_baseId: 		element += dto._uniqID; 		break;
	}
	return element;	
}

float EepromManager::fetchDeviceFloat(EepromPlaceName pName, uint8_t id) {
	float result = -1.0;
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceCtrls: {
			result = memoryDtoUnion.memoryDTO._deviceCtrls[id];
			break;
		}
		case eepr_deviceMins: {
			result = memoryDtoUnion.memoryDTO._deviceMins[id];
			break;
		}
		case eepr_deviceMaxs: {
			result = memoryDtoUnion.memoryDTO._deviceMaxs[id];
			break;
		}
		case eepr_deviceDiscretes: {
			result = memoryDtoUnion.memoryDTO._deviceDiscretes[id];
			break;
		}
	}
	return result;
}

long EepromManager::fetchBoardUID() {
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return memoryDtoUnion.memoryDTO._uniqID;
}

bool EepromManager::fetchDeviceBool(EepromPlaceName pName, uint8_t id) {
	bool result = false; // TODO it is not right to return false in wrong case. False is also a result
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBools: {
			result = memoryDtoUnion.memoryDTO._deviceDigitalBools[id];
			break;
		}		
		case eepr_deviceAnalogBools: {
			result = memoryDtoUnion.memoryDTO._deviceAnalogBools[id];
			break;
		}
		case eepr_deviceAdjustableBools: {
			result = memoryDtoUnion.memoryDTO._deviceAdjustableBools[id];
			break;
		}
		case eepr_deviceRotatableBools: {
			result = memoryDtoUnion.memoryDTO._deviceRotatableBools[id];
			break;
		}
	}
	return result;
}

void EepromManager::fetchIds(uint8_t* pIdsBuffer) {
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		pIdsBuffer[i] = dto._deviceIds[i];
	}
}

int EepromManager::getMaxByteOfPlace(EepromPlaceName pName) {
	int maxNumOfBytes = 0;
	switch (pName) {
		case eepr_target: 	maxNumOfBytes = SERVER_TARGET_NUM_BYTES; 	break;
		case eepr_serverPort: 	maxNumOfBytes = SERVER_PORT_NUM_BYTES; 	break;
		case eepr_serverAdress: maxNumOfBytes = SERVER_ADRESS_MAX_LEN; 	break;
		case eepr_tcpPsswd: 	maxNumOfBytes = TCP_PSSWD_MAX_LEN; 		break;
		case eepr_tcpLogin: 	maxNumOfBytes = TCP_LOGIN_MAX_LEN; 		break;
		case eepr_wifiPsswd: 	maxNumOfBytes = WIFI_PSSWD_MAX_LEN; 	break;
		case eepr_wifiLogin: 	maxNumOfBytes = WIFI_SSID_MAX_LEN; 		break;
		case eepr_deviceIds: 	maxNumOfBytes = MAX_DEVICES; 			break;
		case eepr_baseId: 		maxNumOfBytes = BASE_ID_NUM_BYTES; 		break;
	}
	return maxNumOfBytes;
}

/*****************
* private methods*
******************/

void EepromManager::copy(char* a, int a_len, String& b) {
	int maxCopySymbols = b.length() + 1; // because toCharArray put 0 into last element of a[]
	if (a_len < maxCopySymbols) {
		maxCopySymbols = a_len;
	}
	// Serial.print("EepromManager::copy maxCopySymbols>> "); Serial.println(maxCopySymbols);
	// Serial.print("EepromManager::copy need to be>> "); Serial.println(a_len);
	b.toCharArray(a, maxCopySymbols);
	// Serial.print("EepromManager::copy after copy>> "); Serial.println(a);
}