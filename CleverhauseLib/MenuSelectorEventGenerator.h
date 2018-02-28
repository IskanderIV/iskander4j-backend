// MenuSelectorEventGenerator
// (c) Ivanov Aleksandr, 2018

#ifndef _MenuSelectorEventGenerator_H_
#define _MenuSelectorEventGenerator_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class ControllerSelectorObserver;

class MenuSelectorEventGenerator
{
public:
	MenuSelectorEventGenerator();
	~MenuSelectorEventGenerator();
	
	void notifyMenuSelectorToMoveUp();
	void notifyMenuSelectorToMoveDown();
	void notifyMenuSelectorToGoBack();
	void notifyMenuSelectorToSelect();
	void notifyMenuSelectorToBeShowen();
	void notifyMenuSelectorToBeHidden();
	void addListener(ControllerSelectorObserver* _addedObserver);
		
private:
	ControllerSelectorObserver** _observers;	
	int _numOfListeners;
};

#endif