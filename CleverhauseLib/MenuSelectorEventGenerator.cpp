// MenuSelectorEventGenerator
// (c) Ivanov Aleksandr, 2018

#include "MenuSelectorEventGenerator.h"
#include "ControllerSelectorObserver.h"

MenuSelectorEventGenerator::MenuSelectorEventGenerator() {
	_observers = NULL;
	_numOfListeners = 0;
}

MenuSelectorEventGenerator::~MenuSelectorEventGenerator(){
	if (_observers) {
		// we don't need to delete listeners. Only array of pointers
		delete[] _observers;
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToMoveUp() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->up();
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToMoveDown() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->down();
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToGoBack() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->back();
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToSelect() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->select();
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToBeShowen() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->show();
	}
}

void MenuSelectorEventGenerator::notifyMenuSelectorToBeHidden() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->hide();
	}
}

void MenuSelectorEventGenerator::addListener(ControllerSelectorObserver* _addedObserver) {
	_numOfListeners++;
	if (_observers) {
		ControllerSelectorObserver** temp = new ControllerSelectorObserver*[_numOfListeners];
		for (int i = 0; i < _numOfListeners - 1; i++) {
			temp[i] = _observers[i];
		}
		temp[_numOfListeners - 1] = _addedObserver;
		delete[] _observers;
		_observers = temp;
	} else {		
		_observers = new ControllerSelectorObserver*[_numOfListeners];
		_observers[0] = _addedObserver;
	}
}

