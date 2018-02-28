// ControllerSelectorObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _ControllerSelectorObserver_H_
#define _ControllerSelectorObserver_H_

#include "Object.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class ControllerSelectorObserver : public Object 
{
public:
	virtual void up() = 0;
	virtual void down() = 0;
	virtual void back() = 0;
	virtual void select() = 0;
	virtual void show() = 0;
	virtual void hide() = 0;
	virtual String getCurrMenuName() = 0;
	
protected:
	ControllerSelectorObserver();
	~ControllerSelectorObserver();
	
};

#endif