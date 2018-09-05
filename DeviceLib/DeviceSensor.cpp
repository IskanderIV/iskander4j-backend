// DeviceSensor.cpp
// (c) Ivanov Aleksandr, 2018

#include "DeviceSensor.h"

DeviceSensor::DeviceSensor(uint8_t pPin, DeviceDataBase* pDataBase): 
											_pin(pPin),
											_dataBase(pDataBase) {
	init();
}

DeviceSensor::~DeviceSensor() {
}

void DeviceSensor::init() {
	pinMode(_pin, INPUT);
}

/*****************
* public methods *
******************/


float DeviceSensor::measure() {
	float voltageValue = analogRead(_pin);
	float numberValue = ((VOLTAGE_MAX * voltageValue) / READ_VALUE_MAX);
	
	_dataBase->setDeviceAck(numberValue);
	Serial.print(String(F("Sensor analog read = ")) + _dataBase->getDeviceAck());
}

/******************
* private methods *
*******************/