// DeviceData.cpp
// (c) Ivanov Aleksandr, 2018

#include "DeviceData.h"

DeviceData::DeviceData() {
}

DeviceData::~DeviceData() {	
}
	
String DeviceData::getPayload() {
	return _payload;
}

void DeviceData::setPayload(String& pPayload) {
	_payload = pPayload;
}
