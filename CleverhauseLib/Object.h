// Object
// (c) Ivanov Aleksandr, 2018

#ifndef _Object_H_
#define _Object_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class Object
{
public:
	boolean isActive();
	void setActive(boolean _active);
	
protected:
	Object();
	~Object();
		
private:
	boolean _active;	
};

#endif