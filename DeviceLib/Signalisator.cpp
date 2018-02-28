// Signalisator
// (c) Ivanov Aleksandr, 2018

#include "Signalisator.h"

Signalisator::Signalisator(uint8_t pPin): _pin(pPin) {
	init();
}

Signalisator::~Signalisator() {
}

/*
*	Public interface
*/

void Signalisator::switchOn() {
	if (_currState != ls_BURN) {
		_currState = ls_BURN;
		digitalWrite(_pin, HIGH);
	}
}

void Signalisator::switchOff() {
	if (_currState != ls_OFF) {
		_currState = ls_OFF;
		digitalWrite(_pin, LOW);
	}
}

void Signalisator::blink(uint8_t _freq) {
	//TODO do by using timer
}

/*
*	Private interface
*/

void Signalisator::init() {
	_currState = ls_OFF;
	pinMode(_pin, OUTPUT);
}

