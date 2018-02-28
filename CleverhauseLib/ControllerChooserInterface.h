// ControllerChooserInterface
// (c) Ivanov Aleksandr, 2018

#ifndef _ControllerChooserInterface_H_
#define _ControllerChooserInterface_H_

#include "Object.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif
// class Event;

class ControllerChooserInterface : public Object
{
public:
	virtual void reinit(String* _array, int length) = 0;
	virtual void moveForward() = 0;
	virtual void moveBack() = 0;
	virtual String getCurrElement() = 0;
	
protected:
	ControllerChooserInterface();
	~ControllerChooserInterface();
	
};

#endif