// EepromManager
// (c) Ivanov Aleksandr, 2018

#ifndef _EepromManager_H_
#define _EepromManager_H_

#define MEMORY_BEGIN_POSITION 1
#define BASE_ID_NUM_BYTES 4 //long
#define MAX_DEVICES 8
#define WIFI_SSID_MAX_LEN 80
#define WIFI_PSSWD_MAX_LEN 30
#define TCP_LOGIN_MAX_LEN 50
#define TCP_PSSWD_MAX_LEN 30
#define SERVER_ADRESS_MAX_LEN 30
#define SERVER_PORT_NUM_BYTES 4 //long

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

enum EepromPlaceName {
	eepr_baseId,
	eepr_deviceIds,
	eepr_wifiLogin,
	eepr_wifiPsswd,
	eepr_tcpLogin,
	eepr_tcpPsswd,
	eepr_serverAdress,
	eepr_serverPort
	// TODO target
};

struct MemoryDTO {
		long _uniqID;
		uint8_t _deviceIds[MAX_DEVICES];
		char _wifiLogin[WIFI_SSID_MAX_LEN];
		char _wifiPsswd[WIFI_PSSWD_MAX_LEN];
		char _tcpLogin[TCP_LOGIN_MAX_LEN];
		char _tcpPsswd[TCP_PSSWD_MAX_LEN];		
		char _serverAdress[SERVER_ADRESS_MAX_LEN];		
		long _serverPort;		
};

class EepromManager
{
public:
	EepromManager();
	~EepromManager();
	
	// interface impl for controllers events
	void save(EepromPlaceName _name, String& _data);
	void saveDevicesIds(uint8_t* _idsBuffer);
	String fetch(EepromPlaceName _name);
	void fetchIds(uint8_t* _idsBuffer);
	
	int getMaxByteOfPlace(EepromPlaceName _name);
	
private:
	
	void copy(char* a, int a_len, String& b);
};

#endif