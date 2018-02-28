// MenuSelector
// (c) Ivanov Aleksandr, 2018

#include "Menu.h"
#include "MenuNode.h"
#include "MenuSelector.h"
#include "ControllerEventGenerator.h"
#include "Event.h"

MenuSelector::MenuSelector(){
	_menu = NULL;
	_controllerEG = new ControllerEventGenerator();
	Serial.println("MenuSelector()!");//TEST
}
MenuSelector::~MenuSelector(){
}

void MenuSelector::down(){
	if (!_menu || !isMenuActive()) return; //return "";
	MenuNode* curr = _menu->getCurrActive();
	curr->setActive(false);
	MenuNode* parent = curr->getParent();
	MenuNode* nextActive = NULL;
	if (curr->getNext()) {
		nextActive = curr->getNext();		
	} else {
		nextActive = parent->getFirstChild();
	}
	_menu->setCurrActive(nextActive);
	nextActive->setActive(true);
	String activeTitle = nextActive->getTitle();//TEST
	Serial.println(activeTitle);//TEST
	//return activeTitle;
}

void MenuSelector::up(){
	if (!_menu || !isMenuActive()) return; //return "";
	MenuNode* curr = _menu->getCurrActive();
	curr->setActive(false);
	MenuNode* parent = curr->getParent();
	MenuNode* nextActive = NULL;
	if (curr->getPrev()) {
		nextActive = curr->getPrev();		
	} else {
		nextActive = parent->getLastChild();
	}
	_menu->setCurrActive(nextActive);
	nextActive->setActive(true);
	String activeTitle = nextActive->getTitle();//TEST
	Serial.println(activeTitle);//TEST
	//return activeTitle;
}

void MenuSelector::select(){
	if (!_menu || !isMenuActive()) return; //return "";
	MenuNode* curr = _menu->getCurrActive();
	MenuNode* nextActive = NULL;
	bool selectActionNode = false;
	if (curr->getFirstChild()) {
		nextActive = curr->getFirstChild();
	} else {
		selectActionNode = true;
		nextActive = curr;
	}
	_menu->setCurrActive(nextActive);
	nextActive->setActive(true);
	String activeTitle = nextActive->getTitle();
	Serial.println(activeTitle);//TEST
	//Event* event = new Event(activeTitle);
	if (selectActionNode) {
		if (nextActive->isActionNode()) {
			_controllerEG->notifyControllerAboutAction();
		}		
	} 
	//delete event;
	//return activeTitle;
}

void MenuSelector::show(){
	if (!_menu) return; //return "";
	disactivateMenu();

	MenuNode* curr = _menu->getRoot()->getFirstChild();
	if (curr) {
		curr->setActive(true);
		_menu->setCurrActive(curr);
	}
	Serial.println(curr->getTitle());//TEST
}

void MenuSelector::hide(){
	disactivateMenu();
}

void MenuSelector::disactivateMenu(){
	if (_menu) {
		MenuNode* curr = _menu->getCurrActive();
		if (curr) {
			curr->setActive(false);
		}
		_menu->setCurrActive(NULL);
	}
}

void MenuSelector::back(){
	if (!_menu || !isMenuActive()) return; //return "";
	MenuNode* curr = _menu->getCurrActive();
	MenuNode* nextActive = NULL;
	MenuNode* parent = curr->getParent();
	// if Node is not on High level	
	if (parent->getParent() != NULL) {
		nextActive = parent;
		_menu->setCurrActive(nextActive);
		nextActive->setActive(true);
		String activeTitle = nextActive->getTitle();//TEST
		Serial.println(activeTitle);//TEST
		//return activeTitle;
	} else {
		hide();
		if (_controllerEG) {
			_controllerEG->notifyAboutMenuExit();
		}		
		return; //return "";
	}	
}

boolean MenuSelector::isMenuActive() {
	if (!_menu) return false;
	MenuNode* curr = _menu->getCurrActive();
	boolean activeMenu = curr != NULL ? true : false;
	return activeMenu;
}

void MenuSelector::setMenu(Menu* pMenu) {
	_menu = pMenu;
}

void MenuSelector::setController(SelectorControllerObserver* pController) {
	_controller = pController;
	if (_controller) {
		_controllerEG->addListener(_controller);
	}
}

String MenuSelector::getCurrMenuName() {
	String activeTitle = _menu->getCurrActive()->getTitle();
	return activeTitle;
}


