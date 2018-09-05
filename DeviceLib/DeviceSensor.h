// DeviceSensor.h
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceSensor_H_
#define _DeviceSensor_H_

#define SENSOR_PIN_DEF 7
#define READ_VALUE_MAX 1023
#define READ_VALUE_MIN 0
#define VOLTAGE_MAX 5

#include "DeviceDataBase.h"

class DeviceSensor
{
private:
	DeviceDataBase* _dataBase;
	uint8_t _pin;
	
public:
	DeviceSensor(DeviceDataBase* pDataBase = nullptr);
	~DeviceSensor();
	
	float measure();
	
	
private:
	//methods
	void  init();
};

#endif