// DeviceDigitalActuator
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceDigitalActuator_H_
#define _DeviceDigitalActuator_H_

#define ACTUATOR_PIN_DEF 2
// #define DUTY_CYCLE_MAX 255 // use it only with analog device
// #define DUTY_CYCLE_MID 127 // use it only with analog device
// #define DUTY_CYCLE_MIN 0 // use it only with analog device

#include "ActuatorInterface.h"
#include "DeviceDataBase.h"

//#define DEBUG

class DeviceDigitalActuator : public ActuatorInterface
{
private:
	DeviceDataBase* _dataBase;
	uint8_t  _pin;
	int _prevState;
	// uint8_t  _dutyCycle; // use it only with analog device
	// bool     _isPWM; // ??? use it only with analog device
	
public:
	DeviceDigitalActuator(DeviceDataBase* pDataBase = nullptr);
	~DeviceDigitalActuator();
	
	virtual void process();
	
private:
	//methods
	void init();
	int getSavedControlState();
	void riseUp();
	void fallDawn();
};

#endif