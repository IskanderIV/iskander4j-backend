// DisplayEventGenerator
// (c) Ivanov Aleksandr, 2018

#ifndef _DisplayEventGenerator_H_
#define _DisplayEventGenerator_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class ControllerDisplayObserver;

class DisplayEventGenerator
{
public:
	DisplayEventGenerator(); 
	~DisplayEventGenerator();
	
	void notifyDisplayToShowMenu();
	void notifyDisplayToShowCurrMenu();
	void notifyDisplayToHideMenu();
	void notifyDisplayToShowInitRF();
	void notifyDisplayToShowInitGSM();
	void notifyDisplayToShowInputer();
	void notifyDisplayToShowCurrInputSymbol();
	void notifyDisplayToShowChooser();
	void notifyDisplayToShowCurrChooserElement();
	void addListener(ControllerDisplayObserver* _addedObserver);
		
private:
	ControllerDisplayObserver** _observers;	
	int _numOfListeners;
};

#endif