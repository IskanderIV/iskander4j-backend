// ControllerDisplayObserver
// (c) Ivanov Aleksandr, 2018

#ifndef _ControllerDisplayObserver_H_
#define _ControllerDisplayObserver_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG

class ControllerDisplayObserver
{
public:	
	virtual void showMenu() = 0;
	virtual void showCurrMenu() = 0;
	virtual void hideMenu() = 0;
	virtual void showInitRF() = 0;
	virtual void showInitGSM() = 0;
	virtual void showInputer() = 0;
	virtual void showCurrInputSymbol() = 0;
	virtual void showChooser() = 0;
	virtual void showCurrChooserElement() = 0;
	
protected:
	ControllerDisplayObserver();
	~ControllerDisplayObserver();
	
};

#endif