// ResponseParser.cpp
// (c) Ivanov Aleksandr, 2018

#include "ResponseParser.h"
#include <MemoryFree.h>


ResponseParser::ResponseParser(DataBase* pDataBasePointer)
{
	init(pDataBasePointer);
	Serial.println("ResponseParser()!"); //TEST
}

ResponseParser::~ResponseParser(){
	resetResponse();
}

void ResponseParser::init(DataBase* pDataBasePointer){
	_dataBase = pDataBasePointer;
	_goodParsing = false;
	_maxExpectedDevices = _dataBase->getDeviceCount();
	_deviceInfoArray = new DeviceInfo*[_maxExpectedDevices];
	
	const int maxDevices = _dataBase->getMaxDevices();
	uint8_t deviceIds[maxDevices];
	memset(deviceIds, 0, maxDevices);
	_dataBase->fetchIds(deviceIds);
	
	for(int i = 0; i < _maxExpectedDevices; i++) {
		_deviceInfoArray[i] = new DeviceInfo(deviceIds[i]);
	}	
}

/****************** 
* public interface*
*******************/

void ResponseParser::resetResponse() {
	response = "";
	
	for(int i = 0; i < _dataBase->getDeviceCount(); i++) {
		// Serial.println(String(F("Delete _deviceInfoArray element i = ")) + i);//TEST
		delete _deviceInfoArray[i];
	}
	delete[] _deviceInfoArray;
}

bool ResponseParser::parseResponse(HttpExchangeType type) {
	// Serial.println((String)"RESPONSE>>\n" + response); //TEST
	String headers = "";
	String body = "";
	if(!splitResponse(headers, body)) return false;
	response = "";
	// Serial.println((String)"headers>>\n" + headers); //TEST
	// Serial.println((String)"body>>\n" + body); //TEST
	
	if(parseHeaders(headers)) {
		if (parseBody(type, body)) {
			if (type == DATA) {
				saveData();
			}
			Serial.println(F("<<<Good saving of new data>>>"));//TEST
			return true;
		}
		// Serial.println((String)F("Free memory jsonBuffer>> ") + freeMemory());//TEST		
	}
	return false;
}

void ResponseParser::saveData() {
	for (uint8_t id = 0; id < _dataBase->getDeviceCount(); id++) {
		_dataBase->setDeviceControlValue(_deviceInfoArray[id]->getId(), _deviceInfoArray[id]->getControlVal());
	}
}

/********************
* private
********************/

bool ResponseParser::splitResponse(String& headers, String& body) {
	int beginResponsePos = response.indexOf((char*) HTTP);
	
	if (beginResponsePos == -1) return false;
	
	int endOfHeadersPos = response.indexOf((char*) EMPT_LINE, beginResponsePos);
	
	if (endOfHeadersPos == -1) return false;
	
	headers = response.substring(beginResponsePos, endOfHeadersPos + 2);
	body = response.substring(endOfHeadersPos + 4);	
	
	return true;
}

int ResponseParser::parseHeaders(String& headers) {
	bool result = false;
	
	if (headers.indexOf(OK200) != -1) {
		Serial.println((String)F("Code 200.......... memory>>") + freeMemory());//TEST
		result = true;
	}
	
	return result;
}

bool ResponseParser::parseBody(HttpExchangeType type, String& body) {
	// Serial.println((String)F("Free memory ResponseParser::parseBody begin>> ") + freeMemory());//TEST
	bool parsingResult = false;
	
	if(trimBodyToJson(body)) {
		switch (type) {
			case DATA: 
				parsingResult = findAndSaveData(body); break;
			case REG:
				parsingResult = findAndAnalizeRegMessage(body); break;
		}		
	}
	// Serial.println(String(F("Free memory after parseBody>> ")) + freeMemory());//TEST
	// Serial.println((String)F("Free memory ResponseParser::parseBody end>> ") + freeMemory());//TEST
	return parsingResult;
}

bool ResponseParser::trimBodyToJson(String& body) {
	int begPos = body.indexOf('{');
	int endPos = body.lastIndexOf('}');
	if (begPos == -1 || endPos == -1 ) {
		return false;
	} else {
		body = body.substring(begPos, endPos + 1);	
		Serial.println((String)"body json = >>\n" + body);	
		// body.trim();
		return true;
	}	
}

bool ResponseParser::findAndSaveData(String& body) {	
	int nextElBegPos = 0;
	int id = 0;
	while (nextElBegPos != -1 && id < _maxExpectedDevices) {
		nextElBegPos = findAndRememberDeviceValueByKey(body, id, String(DEVICE_ID_KEY), nextElBegPos);
		// Serial.println(String(DEVICE_ID_KEY) + (String)" nextElBegPos>> " + nextElBegPos);//TEST
		nextElBegPos = findAndRememberDeviceValueByKey(body, id, String(DEVICE_ACK_KEY), nextElBegPos);
		// Serial.println(String(DEVICE_ACK_KEY) + (String)" nextElBegPos>> " + nextElBegPos);//TEST
		nextElBegPos = findAndRememberDeviceValueByKey(body, id, String(DEVICE_ADJUSTABLE_KEY), nextElBegPos);
		// Serial.println(String(DEVICE_ADJUSTABLE_KEY) + (String)" nextElBegPos>> " + nextElBegPos);//TEST
		nextElBegPos = findAndRememberDeviceValueByKey(body, id, String(DEVICE_CONTROL_VALUE_KEY), nextElBegPos);
		// Serial.println(String(DEVICE_CONTROL_VALUE_KEY) + (String)" nextElBegPos>> " + nextElBegPos);//TEST
		nextElBegPos = findAndRememberDeviceValueByKey(body, id, String(DEVICE_RADIO_ERROR_KEY), nextElBegPos);
		// Serial.println(String(DEVICE_RADIO_ERROR_KEY) + (String)" nextElBegPos>> " + nextElBegPos);//TEST
		
		if (nextElBegPos != -1) {
			body = body.substring(nextElBegPos);
			nextElBegPos = 0;
			id++;
		} else {
			// can't parse all data
			return false;
		}		
		// Serial.println((String)"findAndSaveData >> ");//TEST
	}
	return true;
}

int ResponseParser::findAndRememberDeviceValueByKey(String& body, uint8_t id, String key, int begPos) {
	if (begPos == -1) return -1;
	
	String value = "";
	int dataEndPos = findAndFetchValueByKey(body, wrapElement(key), begPos, value);
	
	if (dataEndPos == -1) return -1;
	
	if (!matchAndRememberDeviceElement(id, key, value)) return -1;
	Serial.println((String)">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + key + " >> " + value);
	
	return dataEndPos;
}

int ResponseParser::findAndFetchValueByKey(String& body, String key, int begPos, String& value) {
	// int endBracePos = body.indexOf("}", begPos);
	int keyPos = body.indexOf(key, begPos);
	// if (keyPos == -1 || endBracePos < keyPos) return -1;
	if (keyPos == -1) return -1;
	
	int colonBegPos = keyPos + strlen(key.c_str());
	int colonPos = body.indexOf(":", colonBegPos);
	
	int endPos1 = body.indexOf(",", colonPos + 1);
	int endPos2 = body.indexOf("}", colonPos + 1);
	
	if (endPos1 == -1 && endPos2 == -1) return -1;
	
	int dataBegPos = colonPos + 1;
	int dataEndPos;
	
	if (endPos1 == -1) {
		dataEndPos = endPos2;
	} else {
		dataEndPos = min(endPos1, endPos2);
	}
	
	value = body.substring(dataBegPos, dataEndPos);
	value.trim();
	// Serial.println((String)"" + key + ":" + value + " dataEndPos = " + dataEndPos); //TEST
	
	if (strcmp(value.c_str(), "") == 0) {
		return -1;
	}
	
	return dataEndPos;
}

bool ResponseParser::matchAndRememberDeviceElement(uint8_t id, String key, String& value) {
	bool result = false;
	const char* key_char = key.c_str();
	if (strcmp(key_char, String(DEVICE_ID_KEY).c_str()) == 0) {
		if (value.toInt() == _deviceInfoArray[id]->getId()) result = true;
		// Serial.println((String)"value.toInt() = " + value.toInt()); //TEST
		// Serial.println((String)"_deviceInfoArray[id]->getId() = " + (String)_deviceInfoArray[id]->getId()); //TEST
	} else if (strcmp(key_char, String(DEVICE_ACK_KEY).c_str()) == 0) {
		float val = atof(value.c_str()); // TODO string is not validated
		_deviceInfoArray[id]->setAck(val);
		result = true;
	} else if (strcmp(key_char, String(DEVICE_ADJUSTABLE_KEY).c_str()) == 0) {
		bool val = strcmp(value.c_str(), "true") == 0 ? true : false;
		_deviceInfoArray[id]->setAdjustable(val);
		result = true;
	} else if (strcmp(key_char, String(DEVICE_CONTROL_VALUE_KEY).c_str()) == 0) {
		float val = atof(value.c_str());
		_deviceInfoArray[id]->setControlVal(val);
		result = true;
	} else if (strcmp(key_char, String(DEVICE_RADIO_ERROR_KEY).c_str()) == 0) {		
		bool val = strcmp(value.c_str(), "true") == 0 ? true : false;
		_deviceInfoArray[id]->setRadioError(val);
		result = true;
	}
	
	return result;
}

bool ResponseParser::findAndAnalizeRegMessage(String& body) {
	String value = "";
	if (!findAndFetchValueByKey(body, wrapElement(String(REGISTRATION_MESSAGE_KEY)), 0, value)) {
		return false;
	}
	if (strcmp(value.c_str(), "OK") == 0) {
		return true;
	} else {
		return false;
	}
}

String ResponseParser::wrapElement(String key) {
	return String(F("\"")) + key + F("\"");
}