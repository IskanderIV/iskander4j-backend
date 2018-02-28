// Chooser
// (c) Ivanov Aleksandr, 2018

#include "Chooser.h"
#include "Event.h"

Chooser::Chooser() {	
	_array = NULL;
	_currPosition = 0;
	_numOfElements = 0;
	Serial.println("Chooser()!");//TEST
}

Chooser::~Chooser(){
	if (_array) {
		delete[] _array;
	}
}

void Chooser::reinit(String* pWifiNames, int pCount){
	_currPosition = 0;
	_numOfElements = pCount;
	if (_array) {
		delete[] _array;
	}
	_array = new String[pCount];
	for (int i = 0; i < pCount; i++) {
		String str = pWifiNames[i];
		_array[i] = str;
	}
	Serial.println("HERE Chooser::reinit end");
}

String Chooser::getCurrElement(){
	if (_array && _numOfElements) {
		return _array[_currPosition];
	} else {
		return "";
	}	
}

void Chooser::moveBack(){
	if (_currPosition) {
		_currPosition--;
	}
	Serial.print("Chooser::moveForward() _currPosition >>> "); Serial.println(_currPosition);//TEST
}

void Chooser::moveForward(){
	if (_currPosition < _numOfElements - 1) {
		_currPosition++;
	}
	Serial.print("Chooser::moveBack() _currPosition >>> "); Serial.println(_currPosition);//TEST
}



