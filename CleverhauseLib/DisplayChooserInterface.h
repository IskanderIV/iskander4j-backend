// DisplayChooserInterface
// (c) Ivanov Aleksandr, 2018

#ifndef _DisplayChooserInterface_H_
#define _DisplayChooserInterface_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

class DisplayChooserInterface
{
public:
	virtual String getCurrElement() = 0;
	
protected:
	DisplayChooserInterface();
	~DisplayChooserInterface();
	
};

#endif