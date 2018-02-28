// SelectorControllerObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _SelectorControllerObserver_H_
#define _SelectorControllerObserver_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class SelectorControllerObserver
{
public:
	virtual void executeAction() = 0;
	virtual void processMenuExit() = 0;
	// virtual void askDisplayToShowMenuNode() = 0;
	
protected:
	SelectorControllerObserver();
	~SelectorControllerObserver();
};

#endif