// ControllerInputerObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _ControllerInputerObserver_H_
#define _ControllerInputerObserver_H_

#include "Object.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif
class Event;

class ControllerInputerObserver : public Object
{
public:
	virtual void moveCursorLeft() = 0;
	virtual void moveCursorRight() = 0;
	virtual void changeSymbolUp() = 0;
	virtual void changeSymbolDown() = 0;
	virtual void saveText() = 0;
	virtual void clear(Event* _event) = 0;
	virtual String getSavedText() = 0;
	
protected:
	ControllerInputerObserver();
	~ControllerInputerObserver();
	
};

#endif