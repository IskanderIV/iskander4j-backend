// ControllerEventGenerator
// (c) Ivanov Aleksandr, 2018

#include "ControllerEventGenerator.h"
#include "SelectorControllerObserver.h"
#include "Event.h"

ControllerEventGenerator::ControllerEventGenerator() {
	_observers = NULL;
	_numOfListeners = 0;
}

ControllerEventGenerator::~ControllerEventGenerator(){
	if (_observers) {
		// we don't need to delete listeners. Only array of pointers
		delete[] _observers;
	}
}

void ControllerEventGenerator::notifyControllerAboutAction() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->executeAction();
	}
}

// void ControllerEventGenerator::notifyControllerAboutAdjustableMenuNode() {
	// for (int i = 0; i < _numOfListeners; i++) {
		// _observers[i]->runSingleAction();
	// }
// }

// void ControllerEventGenerator::notifyControllerAboutTextualMenuNode(Event* _event) {
	// for (int i = 0; i < _numOfListeners; i++) {
		// _observers[i]->runInputer(_event);
	// }
// }

// void ControllerEventGenerator::notifyControllerAboutChooserableMenuNode() {
	// for (int i = 0; i < _numOfListeners; i++) {
		// _observers[i]->runChooser();
	// }
// }

void ControllerEventGenerator::notifyAboutMenuExit() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->processMenuExit();
	}
}

void ControllerEventGenerator::addListener(SelectorControllerObserver* _addedObserver) {
	_numOfListeners++;
	if (_observers) {
		SelectorControllerObserver** temp = new SelectorControllerObserver*[_numOfListeners];
		for (int i = 0; i < _numOfListeners - 1; i++) {
			temp[i] = _observers[i];
		}
		temp[_numOfListeners - 1] = _addedObserver;
		delete[] _observers;
		_observers = temp;
	} else {		
		_observers = new SelectorControllerObserver*[_numOfListeners];
		_observers[0] = _addedObserver;
	}
}

