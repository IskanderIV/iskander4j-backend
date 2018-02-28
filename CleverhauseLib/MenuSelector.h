// MenuSelector
// (c) Ivanov Aleksandr, 2018

#ifndef _MenuSelector_H_
#define _MenuSelector_H_

#include "ControllerSelectorObserver.h"
#include "DisplaySelectorObserver.h"

//#define DEBUG

class Menu;
class MenuNode;
class ControllerEventGenerator;
class SelectorControllerObserver;

class MenuSelector : public ControllerSelectorObserver, public DisplaySelectorObserver
{
public:
	MenuSelector();
	~MenuSelector();
	
	// interface impl for controller event 
	virtual void up();
	virtual void down();
	virtual void back();
	virtual void select();
	virtual void show();
	virtual void hide();	
	
	// interface impl for display and controller events 
	virtual String getCurrMenuName();
	
	// helping interface
	void setMenu(Menu* _menu);
	void setController(SelectorControllerObserver* _controller);
	boolean isMenuActive();
	
	
private:
	Menu* _menu;
	ControllerEventGenerator* _controllerEG;
	SelectorControllerObserver* _controller;	
	
	void disactivateMenu();
};

#endif