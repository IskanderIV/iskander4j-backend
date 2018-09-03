// EepromManager
// (c) Ivanov Aleksandr, 2018

#ifndef _EepromManager_H_
#define _EepromManager_H_

#define MEMORY_BEGIN_POSITION 1
#define BASE_ID_NUM_BYTES 4 //long
#define BOARD_UID_SYMBOLS 10 //String
#define MAX_DEVICES 8
#define WIFI_SSID_MAX_LEN 80
#define WIFI_PSSWD_MAX_LEN 30
#define TCP_LOGIN_MAX_LEN 50
#define TCP_PSSWD_MAX_LEN 30
#define SERVER_ADRESS_MAX_LEN 30
#define SERVER_PORT_NUM_BYTES 6 //long
#define SERVER_TARGET_NUM_BYTES 100 //long

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#define BOARD_UID 1010101L

//#define DEBUG

enum EepromPlaceName {
	eepr_baseId,
	eepr_deviceIds,
	eepr_deviceCtrls,
	eepr_deviceMins,
	eepr_deviceMaxs,
	eepr_deviceDiscretes,
	eepr_deviceDigitalBools,
	eepr_deviceAnalogBools,
	eepr_deviceAdjustableBools,
	eepr_deviceRotatableBools,
	eepr_wifiLogin,
	eepr_wifiPsswd,
	eepr_tcpLogin,
	eepr_tcpPsswd,
	eepr_serverAdress,
	eepr_serverPort,
	eepr_target
};

struct MemoryDTO {
		long _uniqID;
		uint8_t _deviceIds[MAX_DEVICES];
		float _deviceCtrls[MAX_DEVICES];
		float _deviceMins[MAX_DEVICES];
		float _deviceMaxs[MAX_DEVICES];
		float _deviceDiscretes[MAX_DEVICES];
		bool _deviceDigitalBools[MAX_DEVICES];
		bool _deviceAnalogBools[MAX_DEVICES];
		bool _deviceAdjustableBools[MAX_DEVICES];
		bool _deviceRotatableBools[MAX_DEVICES];
		char _wifiLogin[WIFI_SSID_MAX_LEN];
		char _wifiPsswd[WIFI_PSSWD_MAX_LEN];
		char _tcpLogin[TCP_LOGIN_MAX_LEN];
		char _tcpPsswd[TCP_PSSWD_MAX_LEN];		
		char _serverAdress[SERVER_ADRESS_MAX_LEN];	
		char _serverPort[SERVER_PORT_NUM_BYTES];
		char _serverTarget[SERVER_PORT_NUM_BYTES];
};

class EepromManager
{
public:
	EepromManager();
	~EepromManager();
	
	// interface impl for controllers events
	bool saveString(EepromPlaceName pName, String& pData);
	void saveBoardUID(long pData);
	bool saveFloat(EepromPlaceName pName, uint8_t id, float pData);
	bool saveBool(EepromPlaceName pName, uint8_t id, bool pFlag);
	void saveDeviceId(uint8_t id);
	bool removeId(EepromPlaceName pName, uint8_t id);
	void saveDevicesIds(uint8_t* _idsBuffer);
	String fetchString(EepromPlaceName pName);
	float fetchDeviceFloat(EepromPlaceName pName, uint8_t id);
	long fetchBoardUID();
	bool fetchDeviceBool(EepromPlaceName pName, uint8_t id);
	void fetchIds(uint8_t* _idsBuffer);
	
	int getMaxByteOfPlace(EepromPlaceName _name);
	
private:
	union MemoryDtoUnion {  
		MemoryDTO memoryDTO;  
		uint8_t byteBuffer[sizeof(MemoryDTO)];  
	} memoryDtoUnion;
	
	void init();
	void copy(char* a, int a_len, String& b);
};

#endif