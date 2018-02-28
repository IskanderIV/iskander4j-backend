// InputerEventGenerator
// (c) Ivanov Aleksandr, 2018

#include "InputerEventGenerator.h"
#include "ControllerInputerObserver.h"
#include "Event.h"

InputerEventGenerator::InputerEventGenerator() {
	_observers = NULL;
	_numOfListeners = 0;
}

InputerEventGenerator::~InputerEventGenerator(){
	if (_observers) {
		// we don't need to delete listeners. Only array of pointers
		delete[] _observers;
	}
}

void InputerEventGenerator::notifyInputerToMoveCursorLeft() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->moveCursorLeft();
	}
}

void InputerEventGenerator::notifyInputerToMoveCursorRight() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->moveCursorRight();
	}
}

void InputerEventGenerator::notifyInputerToChangeSymbUp() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->changeSymbolUp();
	}
}

void InputerEventGenerator::notifyInputerToChangeSymbDown() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->changeSymbolDown();
	}
}

void InputerEventGenerator::notifyInputerToSaveText() {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->saveText();
	}
}

void InputerEventGenerator::notifyInputerToClear(Event* _event) {
	for (int i = 0; i < _numOfListeners; i++) {
		_observers[i]->clear(_event);
	}
}

void InputerEventGenerator::addListener(ControllerInputerObserver* _addedObserver) {
	_numOfListeners++;
	if (_observers) {
		ControllerInputerObserver** temp = new ControllerInputerObserver*[_numOfListeners];
		for (int i = 0; i < _numOfListeners - 1; i++) {
			temp[i] = _observers[i];
		}
		temp[_numOfListeners - 1] = _addedObserver;
		delete[] _observers;
		_observers = temp;
	} else {		
		_observers = new ControllerInputerObserver*[_numOfListeners];
		_observers[0] = _addedObserver;
	}
}

