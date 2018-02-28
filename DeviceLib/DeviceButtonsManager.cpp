// DeviceButtonsManager.cpp
// (c) Ivanov Aleksandr, 2018
#include "DeviceButtonsManager.h"

DeviceButtonsManager::DeviceButtonsManager() {
	_buttonsList = NULL;
	_pushedBtnCodeList = NULL;
	_buttonsCount = 0;
	_lastButton = NULL;
	Serial.println("DeviceButtonsManager()!");//TEST
}

DeviceButtonsManager::~DeviceButtonsManager(){
	if (_buttonsList) {		
		delete[] _buttonsList;
	}	
	if (_pushedBtnCodeList) {
		delete[] _pushedBtnCodeList;
	}
}

/*
	return array buttons code array who is pushed at that moment
*/
ButtonPin* DeviceButtonsManager::processButtons() {
	if(_buttonsCount == 0) return NULL;
	
	memset(_pushedBtnCodeList, (int)0, sizeof(int) * _buttonsCount);
	Button* temp = _buttonsList;
	int count = 0;
	while(temp) {
		if (temp->isPushed()) {
			_pushedBtnCodeList[count++] = temp->getCode();
		}
		temp = temp->getNext();
	}
	
	return _pushedBtnCodeList;
}

void DeviceButtonsManager::createButton(ButtonPin pBtnPin, boolean pHasFicsation) {
	Button* button = new Button(pBtnPin, pHasFicsation);	
	if (_buttonsList) {
		_lastButton->setNext(button);
		button->setPrev(_lastButton);		
	} else {
		_buttonsList = button;
	}
	_lastButton = button;
	_buttonsCount++;
	ButtonPin* pushedBtnCodeListTemp = _pushedBtnCodeList;		
	_pushedBtnCodeList = new ButtonPin[_buttonsCount];	
	if (pushedBtnCodeListTemp) {
		delete[] pushedBtnCodeListTemp;
	}
}

int DeviceButtonsManager::getButtonsCount() {
	return _buttonsCount;
}

DeviceButtonsManager::Button::Button(ButtonPin pBtnPin, boolean pHasFixation) {
	_code = pBtnPin;
	_hasFixation = pHasFixation;
	_btnPushed = false;
	_firstrFrontFix = false;
	_firstrFrontPeak = false;
	_prev = NULL;
	_next = NULL;
	pinMode(_code, INPUT);
}

DeviceButtonsManager::Button::~Button(){
}

boolean DeviceButtonsManager::Button::isPushed() {
	boolean prevPushed = _btnPushed;
	_btnPushed = digitalRead(_code) == HIGH;
	_firstrFrontPeak = _btnPushed ^ (_btnPushed & prevPushed);
	_firstrFrontFix = _firstrFrontPeak ^ _firstrFrontFix;
	
	if (_hasFixation) {
		return _firstrFrontFix;
	} else {
		return _firstrFrontPeak;
	}	
}

int DeviceButtonsManager::Button::getCode() {
	return _code;
}

DeviceButtonsManager::Button* DeviceButtonsManager::Button::getPrev() {
	return _prev;
}

void DeviceButtonsManager::Button::setPrev(Button* prev) {
	_prev = prev;
}
		
DeviceButtonsManager::Button* DeviceButtonsManager::Button::getNext() {
	return _next;
}
		
void DeviceButtonsManager::Button::setNext(Button* next) {
	_next = next;
}