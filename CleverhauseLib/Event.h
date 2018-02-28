// Event
// (c) Ivanov Aleksandr, 2018

#ifndef _Event_H_
#define _Event_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class Event
{
public:
	Event(String& _payload);
	~Event();
	
	String getPayload();
	void setPayload(String& _payload);
	
	
private:
	String	_payload;
};

#endif