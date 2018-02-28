// DisplayEventGenerator
// (c) Ivanov Aleksandr, 2018

#include "DisplayEventGenerator.h"
#include "ControllerDisplayObserver.h"

DisplayEventGenerator::DisplayEventGenerator() {
	_observers = NULL;
	_numOfListeners = 0;
}

DisplayEventGenerator::~DisplayEventGenerator(){
	if (_observers) {
		// we don't need to delete listeners. Only array of pointers
		delete[] _observers;
	}
}

void DisplayEventGenerator::notifyDisplayToShowMenu() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showMenu();
	}
}

void DisplayEventGenerator::notifyDisplayToShowCurrMenu() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showCurrMenu();
	}
}

void DisplayEventGenerator::notifyDisplayToShowInputer() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showInputer();
	}
}

void DisplayEventGenerator::notifyDisplayToShowCurrInputSymbol() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showCurrInputSymbol();
	}
}

void DisplayEventGenerator::notifyDisplayToShowChooser() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showChooser();
	}
}

void DisplayEventGenerator::notifyDisplayToShowCurrChooserElement() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->showCurrChooserElement();
	}
}

void DisplayEventGenerator::notifyDisplayToHideMenu() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->hideMenu();
	}
}

void DisplayEventGenerator::addListener(ControllerDisplayObserver* _addedObserver) {
	_numOfListeners++;
	if (_observers) {
		ControllerDisplayObserver** temp = new ControllerDisplayObserver*[_numOfListeners];
		for (int i = 0; i < _numOfListeners - 1; i++) {
			temp[i] = _observers[i];
		}
		temp[_numOfListeners - 1] = _addedObserver;
		delete[] _observers;
		_observers = temp;
	} else {		
		_observers = new ControllerDisplayObserver*[_numOfListeners];
		_observers[0] = _addedObserver;
	}
}

