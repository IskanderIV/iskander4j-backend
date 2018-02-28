// Chooser
// (c) Ivanov Aleksandr, 2018

#ifndef _Chooser_H_
#define _Chooser_H_

#include "ControllerChooserInterface.h"
#include "DisplayChooserInterface.h"

#define SPACE_SYMBOL 32
//#define DEBUG
// class ControllerInputerObserver;
// class Event;

class Chooser : public ControllerChooserInterface, public DisplayChooserInterface
{
public:
	Chooser();
	~Chooser();
	
	// interface impl for controllers and display events
	virtual void reinit(String* _wifiNames, int _count);
	virtual void moveForward();
	virtual void moveBack();
	virtual String getCurrElement();
	
private:	
	String* _array;
	int	_currPosition;
	int _numOfElements;
};

#endif