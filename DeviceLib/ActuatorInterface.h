// ActuatorInterface
// (c) Ivanov Aleksandr, 2018

#ifndef _ActuatorInterface_H_
#define _ActuatorInterface_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class ActuatorInterface
{
public:
	virtual void  process() = 0;
	
protected:
	ActuatorInterface() {};
	~ActuatorInterface() {};
};

#endif