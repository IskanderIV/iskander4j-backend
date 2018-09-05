// Signalisator.h
// (c) Ivanov Aleksandr, 2018

#ifndef _Signalisator_H_
#define _Signalisator_H_

#define LED_PIN_DEF 13

#define LED_BLINK_RARE_FREG  1.0
#define LED_BLINK_OFTEN_FREG 0.1

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

enum LedState {
	ls_BURN,
	ls_OFF,
	ls_BLINK_RARE,
	ls_BLINK_OFTEN
};

class Signalisator
{
private:
	uint8_t  _pin;
	LedState _prevState;
	unsigned long _previousMillis;
	int _ledState;
	
public:
	Signalisator(uint8_t _pin = LED_PIN_DEF, DeviceDataBase* pDataBase);
	~Signalisator();
	
	void process(LedState currState);	
	
private:
	//methods
	void  init();
	void  blink(LedState _freq);
	void  switchOn();
	void  switchOff();
};

#endif