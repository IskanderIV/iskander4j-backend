// DeviceSensor
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceSensor_H_
#define _DeviceSensor_H_

#define SENSOR_PIN_DEF 7
#define READ_VALUE_MAX 1023
#define READ_VALUE_MIN 0
#define VOLTAGE_MAX 5

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

class DeviceSensor
{
private:
	uint8_t _pin;
	int  	_value;
	
public:
	DeviceSensor(uint8_t _pin = SENSOR_PIN_DEF);
	~DeviceSensor();
	
	float measure();
	
	
private:
	//methods
	void  init();
};

#endif