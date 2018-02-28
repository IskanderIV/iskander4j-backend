// DeviceEepromManager
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceEepromManager_H_
#define _DeviceEepromManager_H_

#define MEMORY_BEGIN_POSITION 1
#define BASE_ID_NUM_BYTES 4 //long
#define DEVICE_ID_LEN 1 //BYTE
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
	eepr_deviceId
};

struct MemoryDTO {
		long _uniqID;
		uint8_t _deviceId;		
};

class DeviceEepromManager
{
public:
	DeviceEepromManager();
	~DeviceEepromManager();
	
	// interface impl for controllers events
	void save(EepromPlaceName _name, long _value);
	long fetch(EepromPlaceName _name);
};

#endif