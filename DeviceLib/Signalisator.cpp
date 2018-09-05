// Signalisator.cpp
// (c) Ivanov Aleksandr, 2018

#include "Signalisator.h"

Signalisator::Signalisator(DeviceDataBase* pDataBase): _dataBase(pDataBase) {
	init();
}

Signalisator::~Signalisator() {
}

void Signalisator::init() {
	_pin = LED_PIN_DEF;
	pinMode(_pin, OUTPUT);
	_prevState = ls_OFF;
	_previousMillis = 0;
	_ledState = LOW;
}

/*****************
* public methods *
******************/


void Signalisator::process(LedState currState) {	
	switch(currState) {
		case ls_BURN: {
			switchOn(); break;
		} 
		case ls_OFF: {
			switchOff(); break;
		} 
		case ls_BLINK_RARE: {
			blink(LED_BLINK_RARE_FREG); break;
		} 
		case ls_BLINK_OFTEN: {
			blink(LED_BLINK_OFTEN_FREG); break;
		}
	}
}

/******************
* private methods *
*******************/

void Signalisator::blink(float interval) {
	unsigned long currentMillis = millis();
	
	
    if (currentMillis - _previousMillis >= interval) {
        _previousMillis = currentMillis;
		if (_ledState == LOW) {
			_ledState = HIGH;
		} else {
			_ledState = LOW;
		}
		digitalWrite(_pin, _ledState);
    }
}

void Signalisator::switchOn() {
	if (_prevState != ls_BURN) {
		_prevState = ls_BURN;
		digitalWrite(_pin, HIGH);
	}
}

void Signalisator::switchOff() {
	if (_prevState != ls_OFF) {
		_prevState = ls_OFF;
		digitalWrite(_pin, LOW);
	}
}