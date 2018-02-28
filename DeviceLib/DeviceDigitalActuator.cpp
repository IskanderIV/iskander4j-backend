// DeviceDigitalActuator
// (c) Ivanov Aleksandr, 2018

#include "DeviceDigitalActuator.h"

DeviceDigitalActuator::DeviceDigitalActuator(uint8_t pPin): _pin(pPin) {
	init();
}

DeviceDigitalActuator::~DeviceDigitalActuator() {
}

/*
*	Public interface
*/

void DeviceDigitalActuator::riseUp() {
	if (!_currState) {
		_currState = true;
		if (_isPWM) {
			// _dutyCycle = from database
			// analogWrite(_pin, _dutyCycle);
		} else {
			digitalWrite(_pin, HIGH);
		}
	}
}

void DeviceDigitalActuator::fallDawn() {
	if (_currState) {
		_currState = false;
		digitalWrite(_pin, LOW);
	}
}

/*
*	Private interface
*/

void DeviceDigitalActuator::init() {
	_currState = false;
	_isPWM = false;
	_dutyCycle = DUTY_CYCLE_MID;
	pinMode(_pin, OUTPUT);
}

