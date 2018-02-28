// DeviceDataBase
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceDataBase_H_
#define _DeviceDataBase_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class DeviceEepromManager;

class DeviceDataBase
{
private:
	DeviceEepromManager* _eepromManager;
	long  _uniqBaseID;
	uint8_t  _deviceId;
	float _deviceAck;
	float _controlValue;
	bool  _adjustable;	
	bool  _rotatable;
	bool  _radioError;
	
public:
	DeviceDataBase();
	~DeviceDataBase();
	
	void  setEepromManager(DeviceEepromManager* pEepromManager);
	long  getUniqBaseID();
	void  setUniqBaseID(long _uniqBaseID);	
	uint8_t  getDeviceID();
	void  setDeviceID(uint8_t _deviceID);
	float getDeviceAck();
	void  setDeviceAck(float _ack);
	float getDeviceControlValue();
	void  setDeviceControlValue(float _controlValue);
	bool  isDeviceAdj();
	void  setDeviceAdj(bool _adjustable);
	bool  isDeviceRot();
	void  setDeviceRot(bool _rotatable);
	bool  isDeviceRFErr();
	void  setDeviceRFErr(bool _radioError);
	
private:
	//methods

};

#endif