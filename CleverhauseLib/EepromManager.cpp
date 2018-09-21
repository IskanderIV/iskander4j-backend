// EepromManager
// (c) Ivanov Aleksandr, 2018

#include "EepromManager.h"
#include <EEPROM.h>

EepromManager::EepromManager() {
	init();
	Serial.println("EepromManager()!");//TEST
}

EepromManager::~EepromManager() {
}

void EepromManager::init() {
	initMemoryDto();
	//saveBoardUID(BOARD_UID);// need rewrite by value from server when registration occur
	
	//STUB EEPROM DATA
	memoryDtoUnion.memoryDTO._uniqID = 1168396L;
	char wifiLogin[] = "RAZVRAT_HOUSE"; 
	// char wifiLogin[] = "acer Liquid Z630";
	char wifiPsswd[] = "LaserJet";
	// char wifiPsswd[] = "111222333";
	// saveString(eepr_wifiLogin, wifiLogin);
	// saveString(eepr_wifiPsswd, wifiPsswd);

	char username[] = "iskander";
	char password[] = "123";
	// saveString(eepr_tcpLogin, username);
	// saveString(eepr_tcpPsswd, password);
	
	char host[] = "cleverhause.ru";
	char port[] = "80";
	char target[] = "/cleverhause/boards/board/data";
	// saveString(eepr_serverAdress, tcpServerIP);
	// saveString(eepr_serverPort, tcpServerPort);
	// saveString(eepr_target, tcpServertarget);
	strncpy(memoryDtoUnion.memoryDTO._wifiLogin, wifiLogin, WIFI_SSID_MAX_LEN);
	strncpy(memoryDtoUnion.memoryDTO._wifiPsswd, wifiPsswd, WIFI_PSSWD_MAX_LEN);
	strncpy(memoryDtoUnion.memoryDTO._tcpLogin, username, TCP_LOGIN_MAX_LEN);
	strncpy(memoryDtoUnion.memoryDTO._tcpPsswd, password, TCP_PSSWD_MAX_LEN);
	strncpy(memoryDtoUnion.memoryDTO._serverAdress, host, SERVER_ADRESS_MAX_LEN);
	strncpy(memoryDtoUnion.memoryDTO._serverPort, port, SERVER_PORT_NUM_BYTES);
	strncpy(memoryDtoUnion.memoryDTO._serverTarget, target, SERVER_TARGET_NUM_BYTES);
	
	const int max_devices = (int) MAX_DEVICES;	
	// saveDevicesIds(deviceIds);
	uint8_t deviceIds[max_devices] = {0,0,0,0,0,0,0,0};
	float deviceCtrls[max_devices] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceMins[max_devices] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceMaxs[max_devices] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	float deviceDiscretes[max_devices] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	uint8_t deviceDigitalBools[max_devices] = {0,0,0,0,0,0,0,0};
	uint8_t deviceAnalogBools[max_devices] = {0,0,0,0,0,0,0,0};
	uint8_t deviceAdjustableBools[max_devices] = {0,0,0,0,0,0,0,0};
	uint8_t deviceRotatableBools[max_devices] = {0,0,0,0,0,0,0,0};
	
	for (uint8_t i = 0; i < max_devices; i++) {
		memoryDtoUnion.memoryDTO._deviceIds[i] = deviceIds[i];
		memoryDtoUnion.memoryDTO._deviceCtrls[i] = deviceCtrls[i];
		memoryDtoUnion.memoryDTO._deviceMins[i] = deviceMins[i];
		memoryDtoUnion.memoryDTO._deviceMaxs[i] = deviceMaxs[i];	
		memoryDtoUnion.memoryDTO._deviceDiscretes[i] = deviceDiscretes[i];
		memoryDtoUnion.memoryDTO._deviceDigitalBools[i] = deviceDigitalBools[i];
		memoryDtoUnion.memoryDTO._deviceAnalogBools[i] = deviceAnalogBools[i];
		memoryDtoUnion.memoryDTO._deviceAdjustableBools[i] = deviceAdjustableBools[i];
		memoryDtoUnion.memoryDTO._deviceRotatableBools[i] = deviceRotatableBools[i];
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

void EepromManager::initMemoryDto() {
	const int max_devices = (int) MAX_DEVICES;
	memoryDtoUnion.memoryDTO._uniqID = BOARD_UID;
	for (uint8_t i = 0; i < max_devices; i++) {
		memoryDtoUnion.memoryDTO._deviceIds[i] = 0;
		memoryDtoUnion.memoryDTO._deviceCtrls[i] = 0.0;
		memoryDtoUnion.memoryDTO._deviceMins[i] = 0.0;
		memoryDtoUnion.memoryDTO._deviceMaxs[i] = 0.0;	
		memoryDtoUnion.memoryDTO._deviceDiscretes[i] = 0.0;
		memoryDtoUnion.memoryDTO._deviceDigitalBools[i] = 0;
		memoryDtoUnion.memoryDTO._deviceAnalogBools[i] = 0;
		memoryDtoUnion.memoryDTO._deviceAdjustableBools[i] = 0;
		memoryDtoUnion.memoryDTO._deviceRotatableBools[i] = 0;
		// Serial.println("Cycle i = " + String(i));
	}
	
	initStringPlace(eepr_wifiLogin, memoryDtoUnion.memoryDTO._wifiLogin);
	initStringPlace(eepr_wifiPsswd, memoryDtoUnion.memoryDTO._wifiPsswd);
	initStringPlace(eepr_tcpLogin, memoryDtoUnion.memoryDTO._tcpLogin);
	initStringPlace(eepr_tcpPsswd, memoryDtoUnion.memoryDTO._tcpPsswd);
	initStringPlace(eepr_serverAdress, memoryDtoUnion.memoryDTO._serverAdress);
	initStringPlace(eepr_serverPort, memoryDtoUnion.memoryDTO._serverPort);
	initStringPlace(eepr_target, memoryDtoUnion.memoryDTO._serverTarget);
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
			strncpy(memoryDtoUnion.memoryDTO._serverTarget, pData.c_str(), SERVER_TARGET_NUM_BYTES); 
			result = true;
			break;
		}
		case eepr_serverPort: 	{
			strncpy(memoryDtoUnion.memoryDTO._serverPort, pData.c_str(), SERVER_PORT_NUM_BYTES); 
			result = true;
			break;
		}
		case eepr_serverAdress:	{
			strncpy(memoryDtoUnion.memoryDTO._serverAdress, pData.c_str(), SERVER_ADRESS_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_tcpPsswd: {
			strncpy(memoryDtoUnion.memoryDTO._tcpPsswd, pData.c_str(), TCP_PSSWD_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_tcpLogin: {
			strncpy(memoryDtoUnion.memoryDTO._tcpLogin, pData.c_str(), TCP_LOGIN_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_wifiPsswd: {
			strncpy(memoryDtoUnion.memoryDTO._wifiPsswd, pData.c_str(), WIFI_PSSWD_MAX_LEN); 
			result = true;
			break;
		}
		case eepr_wifiLogin: {
			strncpy(memoryDtoUnion.memoryDTO._wifiLogin, pData.c_str(), WIFI_SSID_MAX_LEN); 
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
	uint8_t flag = btoi(pFlag);
	// Serial.print("EepromManager::save raw data string>> ");
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_deviceDigitalBools: {
			memoryDtoUnion.memoryDTO._deviceDigitalBools[id] = flag; 
			result = true;
			break;
		}		
		case eepr_deviceAnalogBools: {
			memoryDtoUnion.memoryDTO._deviceAnalogBools[id] = flag; 
			result = true; 
			break;
		}
		case eepr_deviceAdjustableBools: {
			memoryDtoUnion.memoryDTO._deviceAdjustableBools[id] = flag;
			result = true;
			break;
		}
		case eepr_deviceRotatableBools: {
			memoryDtoUnion.memoryDTO._deviceRotatableBools[id] = flag;
			result = true;
			break;
		}
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	return result;
}

// void EepromManager::saveId(uint8_t id) {
	// Serial.print("EepromManager::save raw data string>> ");
	// EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	// memoryDtoUnion.memoryDTO._deviceIds[id] = id;
	// EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
// }

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
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		Serial.print(String(F("EepromManager::saveDevicesIds id")) + i + " = ");//TEST 
		Serial.println(pIdsBuffer[i]);//TEST
		memoryDtoUnion.memoryDTO._deviceIds[i] = pIdsBuffer[i];
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
}

String EepromManager::fetchString(EepromPlaceName pName) {
	String element = "";
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	switch (pName) {
		case eepr_target: 		element += memoryDtoUnion.memoryDTO._serverTarget; 	break;
		case eepr_serverPort: 	element += memoryDtoUnion.memoryDTO._serverPort; 	break;
		case eepr_serverAdress:	element += memoryDtoUnion.memoryDTO._serverAdress; 	break;
		case eepr_tcpPsswd: 	element += memoryDtoUnion.memoryDTO._tcpPsswd; 		break;
		case eepr_tcpLogin: 	element += memoryDtoUnion.memoryDTO._tcpLogin; 		break;
		case eepr_wifiPsswd: 	element += memoryDtoUnion.memoryDTO._wifiPsswd; 	break;
		case eepr_wifiLogin: 	element += memoryDtoUnion.memoryDTO._wifiLogin; 	break;
	}
	return element;	
}

float EepromManager::fetchDeviceFloat(EepromPlaceName pName, uint8_t id) {
	float result = 0.0;
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
			result = itob(memoryDtoUnion.memoryDTO._deviceDigitalBools[id]);
			break;
		}		
		case eepr_deviceAnalogBools: {
			result = itob(memoryDtoUnion.memoryDTO._deviceAnalogBools[id]);
			break;
		}
		case eepr_deviceAdjustableBools: {
			result = itob(memoryDtoUnion.memoryDTO._deviceAdjustableBools[id]);
			break;
		}
		case eepr_deviceRotatableBools: {
			result = itob(memoryDtoUnion.memoryDTO._deviceRotatableBools[id]);
			break;
		}
	}
	return result;
}

void EepromManager::fetchIds(uint8_t* pIdsBuffer) {
	EEPROM.get(MEMORY_BEGIN_POSITION, memoryDtoUnion.byteBuffer);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		pIdsBuffer[i] = memoryDtoUnion.memoryDTO._deviceIds[i];
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
		case eepr_baseId: 		maxNumOfBytes = BOARD_UID_SYMBOLS; 		break;
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