// Signalisator
// (c) Ivanov Aleksandr, 2018

#ifndef _Signalisator_H_
#define _Signalisator_H_

#define LED_PIN_DEF 13

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include "ActuatorInterface.h"

//#define DEBUG

enum LedState {
	ls_BURN,
	ls_OFF,
	ls_BLINK
};

class Signalisator
{
private:
	uint8_t  _pin;
	LedState _currState;
	
public:
	Signalisator(uint8_t _pin = LED_PIN_DEF);
	~Signalisator();
	
	void  switchOn();
	void  switchOff();
	void  blink(uint8_t _freq);
	
	
private:
	//methods
	void  init();
};

#endif