// DisplayInputerObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _DisplayInputerObserver_H_
#define _DisplayInputerObserver_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

class DisplayInputerObserver
{
public:
	virtual char getCurrSymbol() = 0;
	virtual int getCurrPosition() = 0;
	virtual const char* getCurrString() = 0;
	
protected:
	DisplayInputerObserver();
	~DisplayInputerObserver();
	
};

#endif