// DeviceDataBase
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceDataBase_H_
#define _DeviceDataBase_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include "DeviceEepromManager.h"

class DeviceEepromManager;

class DeviceDataBase
{
private:
	DeviceEepromManager* _eepromMngr;
	
	long 	_boardUID;
	uint8_t _deviceId;
	float 	_min;
	float 	_max;
	float 	_discrete;
	float 	_deviceAck;
	float 	_controlValue;
	bool 	_digital;	
	bool 	_analog;	
	bool 	_adjustable;	
	bool 	_rotatable;
	bool 	_radioError;
	
public:
	DeviceDataBase(DeviceEepromManager* _eepromMngr);
	~DeviceDataBase();

	long getBoardUID(); //memorized
	void setBoardUID(long _boardUID);	
	
	uint8_t getDeviceId(); //memorized
	void setDeviceId(uint8_t _deviceId);
	
	// float 
	float getDeviceAck();
	void setDeviceAck(float _ack);
	float getDeviceMin();//memorized
	void setDeviceMin(float _min); 
	float getDeviceMax();//memorized
	void setDeviceMax(float _max);
	float getDeviceDiscrete();//memorized
	void setDeviceDiscrete(float _discrete);
	float getDeviceControlValue();//memorized
	void setDeviceControlValue(float _controlValue);
	
	// bool
	bool getDeviceDigital();//memorized
	void setDeviceDigital(bool _digital);
	bool getDeviceAnalog();//memorized
	void setDeviceAnalog(bool _analog);
	bool getDeviceAdj();//memorized
	void setDeviceAdj(bool _adjustable); 
	bool getDeviceRotatable();//memorized
	void setDeviceRotatable(bool _rotatable); 
	bool getDeviceRFErr();
	void setDeviceRFErr(bool _radioError);
	
private:
	//methods
	void init(DeviceEepromManager* pEepromMngr);
	void initFromEeprom();
};

#endif