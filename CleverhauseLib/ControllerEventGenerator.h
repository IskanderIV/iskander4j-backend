// ControllerEventGenerator
// (c) Ivanov Aleksandr, 2018

#ifndef _ControllerEventGenerator_H_
#define _ControllerEventGenerator_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class SelectorControllerObserver;
class Event;

class ControllerEventGenerator
{
public:
	ControllerEventGenerator();
	~ControllerEventGenerator();
	
	// interface for notifying controller about diff types of menu selection
	// void notifyControllerAboutUsualMenuNode();
	void notifyControllerAboutAction();
	//void notifyControllerAboutTextualMenuNode(Event* _event);
	//void notifyControllerAboutChooserableMenuNode();
	void notifyAboutMenuExit();
	void addListener(SelectorControllerObserver* _addedObserver);
		
private:
	SelectorControllerObserver** _observers;	
	int _numOfListeners;
};

#endif