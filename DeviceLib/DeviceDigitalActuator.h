// DeviceDigitalActuator
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceDigitalActuator_H_
#define _DeviceDigitalActuator_H_

#define ACTUATOR_PIN_DEF 2
#define DUTY_CYCLE_MAX 255
#define DUTY_CYCLE_MID 127
#define DUTY_CYCLE_MIN 0

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include "ActuatorInterface.h"

//#define DEBUG

class DeviceDigitalActuator : public ActuatorInterface
{
private:
	uint8_t  _pin;
	uint8_t  _dutyCycle;
	bool     _isPWM;
	bool     _currState;
	
public:
	DeviceDigitalActuator(uint8_t _pin = ACTUATOR_PIN_DEF);
	~DeviceDigitalActuator();
	
	virtual void  riseUp();
	virtual void  fallDawn();
	
	
private:
	//methods
	void  init();
};

#endif