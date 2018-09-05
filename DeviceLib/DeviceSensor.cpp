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
	float numberValue = ((VOLTAGE_MAX * voltageValue) / READ_VALUE_MAX);
	
	_dataBase->setDeviceAck(numberValue);
	Serial.print(String(F("Sensor analog read = ")) + _dataBase->getDeviceAck());
}

/******************
* private methods *
*******************/