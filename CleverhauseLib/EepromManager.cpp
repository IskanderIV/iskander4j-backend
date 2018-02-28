// EepromManager
// (c) Ivanov Aleksandr, 2018

#include "EepromManager.h"
#include <EEPROM.h>

EepromManager::EepromManager() {
	Serial.println("EepromManager()!");//TEST
}

EepromManager::~EepromManager() {
}

/* 
* interface impl for controllers requests 
*
*/
	// TODO Clean memory place before put
void EepromManager::save(EepromPlaceName pName, String& pData) {
	// Serial.print("EepromManager::save raw data string>> "); Serial.println(pData);
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	switch (pName) {
		case eepr_serverPort: 	dto._serverPort = pData.toInt();  						break;
		case eepr_serverAdress:	copy(dto._serverAdress, SERVER_ADRESS_MAX_LEN, pData); 	break;
		case eepr_tcpPsswd: 	copy(dto._tcpPsswd, TCP_PSSWD_MAX_LEN, pData); 			break;
		case eepr_tcpLogin: 	copy(dto._tcpLogin, TCP_LOGIN_MAX_LEN, pData); 			break;
		case eepr_wifiPsswd: 	copy(dto._wifiPsswd, WIFI_PSSWD_MAX_LEN, pData); 		break;
		case eepr_wifiLogin: 	copy(dto._wifiLogin, WIFI_SSID_MAX_LEN, pData); 		break;
		case eepr_baseId: 		dto._uniqID = pData.toInt();  							break;
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, dto);
}

void EepromManager::saveDevicesIds(uint8_t* pIdsBuffer) {
	// Serial.print("EepromManager::save raw data string>> "); Serial.println(pData);
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		Serial.print(String("EepromManager::saveDevicesIds id") + i + " = "); Serial.println(pIdsBuffer[i]);//TEST
		dto._deviceIds[i] = pIdsBuffer[i];
	}
	EEPROM.put(MEMORY_BEGIN_POSITION, dto);
}

String EepromManager::fetch(EepromPlaceName pName) {
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

void EepromManager::fetchIds(uint8_t* pIdsBuffer) {
	String element = "";
	MemoryDTO dto;
	EEPROM.get(MEMORY_BEGIN_POSITION, dto);
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		pIdsBuffer[i] = dto._deviceIds[i];
	}
}

int EepromManager::getMaxByteOfPlace(EepromPlaceName pName) {
	int maxNumOfBytes = 0;
	switch (pName) {
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

/* 
* private methods
*
*/

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

