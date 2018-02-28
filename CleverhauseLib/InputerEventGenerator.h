// InputerEventGenerator
// (c) Ivanov Aleksandr, 2018

#ifndef _InputerEventGenerator_H_
#define _InputerEventGenerator_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class ControllerInputerObserver;
class Event;

class InputerEventGenerator
{
public:
	InputerEventGenerator();
	~InputerEventGenerator();
	
	void notifyInputerToMoveCursorLeft();
	void notifyInputerToMoveCursorRight();
	void notifyInputerToChangeSymbUp();
	void notifyInputerToChangeSymbDown();
	void notifyInputerToSaveText();
	void notifyInputerToClear(Event* _event);
	void addListener(ControllerInputerObserver* _addedObserver);
		
private:
	ControllerInputerObserver** _observers;	
	int _numOfListeners;
};

#endif