// DeviceSensor
// (c) Ivanov Aleksandr, 2018

#include "DeviceSensor.h"

DeviceSensor::DeviceSensor(uint8_t pPin): _pin(pPin) {
	init();
}

DeviceSensor::~DeviceSensor() {
}

/*
*	Public interface
*/

float DeviceSensor::measure() {
	_value = analogRead(_pin);
	float result = ((VOLTAGE_MAX * _value) / READ_VALUE_MAX);
	Serial.print("Sensor analog read = "); Serial.println(result); 
	return result;
}

/*
*	Private interface
*/

void DeviceSensor::init() {
	pinMode(_pin, INPUT);
}

