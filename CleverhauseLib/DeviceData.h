// DeviceData.h
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceData_H_
#define _DeviceData_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class DeviceData
{
public:
	DeviceData();
	~DeviceData();
};

#endif