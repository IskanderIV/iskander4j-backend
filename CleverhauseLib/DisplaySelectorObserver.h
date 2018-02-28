// DisplaySelectorObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _DisplaySelectorObserver_H_
#define _DisplaySelectorObserver_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

class DisplaySelectorObserver
{
public:
	virtual String getCurrMenuName() = 0;
	
protected:
	DisplaySelectorObserver();
	~DisplaySelectorObserver();
	
};

#endif