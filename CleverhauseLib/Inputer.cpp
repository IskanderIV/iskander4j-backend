// Inputer
// (c) Ivanov Aleksandr, 2018

#include "Inputer.h"
#include "Event.h"

Inputer::Inputer() {
	_currInputTextLength = 80;
	_inputText = NULL;
	_symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%^&+=_";
	// memset(_inputText, 0, _maxInputTextLength);
	_currPosition = 0;
	_currSymbol = SPACE_SYMBOL;
	Serial.println("Inputer()!");//TEST
}

Inputer::~Inputer(){
	if (_inputText) {
		delete[] _inputText;
	}
	delete[] _symbols;
}

void Inputer::moveCursorLeft(){
	if (_currPosition) {
		_currPosition--;
	} 
	// else {		
		// _currPosition = getCurrLastPosition();
	// }
	_currSymbol = _inputText[_currPosition];
	// Serial.print("Inputer::moveCursorLeft() _currPosition >>> "); Serial.println(_currPosition);//TEST
	// Serial.print("Inputer::moveCursorLeft() _currSymbol >>> "); Serial.println(_currSymbol);//TEST
}

void Inputer::moveCursorRight(){
	_currPosition++;
	// if (_currPosition == _maxInputTextLength || _currPosition > getCurrLastPosition() + 1) {
		// _currPosition = 0;
	// }
	_currSymbol = _inputText[_currPosition];
	// Serial.print("Inputer::moveCursorRight() _currPosition >>> "); Serial.println(_currPosition);//TEST
	// Serial.print("Inputer::moveCursorRight() _currSymbol >>> "); Serial.println(_currSymbol);//TEST
}

void Inputer::changeSymbolUp(){
	char currSymbol = _inputText[_currPosition];
	if (currSymbol == SPACE_SYMBOL) { // space
		currSymbol = _symbols[0];
	} else {
		for (int i = 0; i < strlen(_symbols); i++) {
			if (currSymbol == _symbols[i]) {
				if (i == strlen(_symbols) - 1) {
					currSymbol = _symbols[0];
				} else {
					currSymbol = _symbols[i+1];
				}				
				break;
			}
		}
	}
	_currSymbol = currSymbol;
	_inputText[_currPosition] = currSymbol;
}

void Inputer::changeSymbolDown(){
	char currSymbol = _inputText[_currPosition];
	if (currSymbol == SPACE_SYMBOL) { // space
		currSymbol = _symbols[strlen(_symbols) - 1];
	} else {
		for (int i = 0; i < strlen(_symbols); i++) {
			if (currSymbol == _symbols[i]) {
				if (i == 0) {
					currSymbol = _symbols[strlen(_symbols) - 1];
				} else {
					currSymbol = _symbols[i-1];
				}				
				break;
			}
		}
	}
	_currSymbol = currSymbol;
	_inputText[_currPosition] = currSymbol;
}

void Inputer::saveText(){
	int count = 0;
	for (int i = 0; i < _currInputTextLength; i++) {
		if (_inputText[i] == SPACE_SYMBOL) break;
		count++;
	}
	char savedText[count];
	for (int i = 0; i < count; i++) {
		savedText[i] = _inputText[i];
	}
	_savedText = String(savedText);
	Serial.print("Inputer::_savedText>>> "); Serial.println(_savedText);//TEST
}

void Inputer::clear(Event* pEvent){
	_currPosition = 0;
	_currSymbol = SPACE_SYMBOL;
	_currInputTextLength = (pEvent->getPayload()).toInt();
	if (_inputText) {
		delete _inputText;
	}
	_inputText = new char[_currInputTextLength];
	memset(_inputText, SPACE_SYMBOL, _currInputTextLength);
}
	
int Inputer::getCurrPosition(){
	return _currPosition;
}

char Inputer::getCurrSymbol(){
	return _currSymbol;
}

const char* Inputer::getCurrString(){
	return _inputText;
}

String Inputer::getSavedText(){
	return _savedText;
}

int Inputer::getCurrLastPosition() {
	int currLastPosition = 0;
		for (int i = _currInputTextLength - 1; i > 0; i--) {
			if (_inputText[i] != 0) {
				currLastPosition = i;
			}
		}
	return currLastPosition;
}

// void Inputer::setMaxInputTextLength(int pMaxInputTextLength){
	// _maxInputTextLength = pMaxInputTextLength;
	// delete _inputText;
	// _inputText = new char[_maxInputTextLength];
	// memset(_inputText, SPACE_SYMBOL, _maxInputTextLength);
// }

// void Inputer::setTextBuffer(char* pTextBuffer){
	// _textBuffer = pTextBuffer;
// }


