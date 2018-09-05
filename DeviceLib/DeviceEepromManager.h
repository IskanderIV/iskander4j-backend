// DeviceEepromManager
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceEepromManager_H_
#define _DeviceEepromManager_H_

#define MEMORY_BEGIN_POSITION 1
#define DEVICE_ID_LEN 1 //BYTE

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

enum EepromPlaceName {
	eepr_baseId,
	eepr_deviceId,
	eepr_deviceCtrl,
	eepr_deviceMin,
	eepr_deviceMax,
	eepr_deviceDiscrete,
	eepr_deviceDigitalBool,
	eepr_deviceAnalogBool,
	eepr_deviceAdjustableBool,
	eepr_deviceRotatableBool
};

struct MemoryDTO {
	long _uniqID;
	uint8_t _deviceId;
	float _deviceCtrl;
	float _deviceMin;
	float _deviceMax;
	float _deviceDiscrete;
	bool _deviceDigitalBool;
	bool _deviceAnalogBool;
	bool _deviceAdjustableBool;
	bool _deviceRotatableBool;		
};

class DeviceEepromManager
{
public:
	DeviceEepromManager();
	~DeviceEepromManager();
	
	// interface
	void saveBoardUID(long pData);
	bool saveFloat(EepromPlaceName pName, float pData);
	bool saveBool(EepromPlaceName pName, bool pFlag);
	void saveDeviceId(uint8_t id);
	float fetchFloat(EepromPlaceName pName);
	bool fetchBool(EepromPlaceName pName);
	long fetchBoardUID();
	uint8_t fetchDeviceId();
	
private:
	union MemoryDtoUnion {  
		MemoryDTO memoryDTO;  
		uint8_t byteBuffer[sizeof(MemoryDTO)];  
	} memoryDtoUnion;
	
	void init();
};

#endif