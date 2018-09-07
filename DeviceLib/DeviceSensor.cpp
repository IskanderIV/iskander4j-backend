// DeviceSensor.cpp
// (c) Ivanov Aleksandr, 2018

#include "DeviceSensor.h"

DeviceSensor::DeviceSensor(DeviceDataBase* pDataBase): _dataBase(pDataBase) {
	init();
}

DeviceSensor::~DeviceSensor() {
}

void DeviceSensor::init() {
	_pin = SENSOR_PIN_DEF;
	pinMode(_pin, INPUT);
}

/*****************
* public methods *
******************/


float DeviceSensor::measure() {
	float voltageValue = analogRead(_pin);
	Serial.println(String(F("voltageValue = ")) + voltageValue); // TEST
	
	float numberValue = ((VOLTAGE_MAX * voltageValue) / READ_VALUE_MAX);
	Serial.println(String(F("numberValue = ")) + numberValue); // TEST
	
	_dataBase->setDeviceAck(numberValue);
	Serial.println(String(F("Sensor analog read = ")) + _dataBase->getDeviceAck());
}

/******************
* private methods *
*******************/