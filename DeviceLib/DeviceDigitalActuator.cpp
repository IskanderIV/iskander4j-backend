// DeviceDigitalActuator
// (c) Ivanov Aleksandr, 2018

#include "DeviceDigitalActuator.h"

DeviceDigitalActuator::DeviceDigitalActuator(DeviceDataBase* pDataBase): _dataBase(pDataBase) {
	init();
}

DeviceDigitalActuator::~DeviceDigitalActuator() {
}

void DeviceDigitalActuator::init() {
	_pin = ACTUATOR_PIN_DEF;
	pinMode(_pin, OUTPUT);
	_prevState = getSavedControlState();
	if (_prevState == 1) {
		riseUp();
	} else {
		fallDawn();
	}
	// _dutyCycle = DUTY_CYCLE_MID; // use it only with analog device
	
}

/*****************
* public methods *
******************/

void DeviceDigitalActuator::process() {
	int currState = getSavedControlState();
	
	if (_prevState == currState) return;
	
	_prevState = currState;
	
	if (currState == 1) {
		riseUp();
	}
	if (currState == 0) {
		fallDawn();
	}
}

/******************
* private methods *
*******************/


int DeviceDigitalActuator::getSavedControlState() {
	float cntrlValue = _dataBase->getDeviceControlValue();
	if (cntrlValue > 0.9 && cntrlValue < 1.1) {
		return 1;
	} else if (cntrlValue > -0.1 && cntrlValue < 0.1) {
		return 0;
	} else {
		return -1;
	}
}

void DeviceDigitalActuator::riseUp() {
	digitalWrite(_pin, HIGH);
}

void DeviceDigitalActuator::fallDawn() {
	digitalWrite(_pin, LOW);
}