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
	_deviceInfoArray = new DeviceInfo*[_dataBase->getDeviceCount()];
	_goodParsing = false;
}

/****************** 
* public interface*
*******************/

void ResponseParser::resetResponse() {
	response = "";
	headers = "";
	body = "";
	_goodParsing = false;
	for(int i = 0; i < _dataBase->getDeviceCount(); i++) {
		delete _deviceInfoArray[i];
	}
	delete[] _deviceInfoArray;
}

bool ResponseParser::parseResponse(HttpExchangeType type) {
	// Serial.println((String)"Response>>" + response);
	char* emptyLine = (char*)"\r\n\r\n";
	int emptyLinePos = response.indexOf(emptyLine);
	headers = response.substring(0, emptyLinePos + 2);
	// Serial.println((String)"headers>>\n" + headers);
	if(parseHeaders()) {
		int bodyBegPos = emptyLinePos + strlen(emptyLine);
		body = response.substring(bodyBegPos);
		response = "";
		headers = "";
		bool result = parseBody(type);
		if (result == true && type == DATA) {
			_goodParsing = true;
			saveData();
			Serial.println(F("<<<<<<<<<<<<Good saving of new data>>>>>>>>>>>>"));
		}
		Serial.println((String)"Free memory jsonBuffer>> " + freeMemory());
		return result;
	} else {
		return false;
	}
}

void ResponseParser::saveData() {
	if (_goodParsing) {
		for (uint8_t id=0; id < _dataBase->getDeviceCount(); id++) {
			_dataBase->setDeviceControlValue(id, _deviceInfoArray[id]->getControlVal());
		}	
	}	
}

/********************
* private
********************/

bool ResponseParser::parseHeaders() {
	headers = "";
	return true;
}

bool ResponseParser::parseBody(HttpExchangeType type) {
	bool parsingResult;
	if(limitBodyToJson()) {
		switch (type) {
			case DATA: 
				parsingResult = findAndSaveData(); break;
			case REG:
				parsingResult = findAndAnalizeRegMessage(); break;
		}		
	} else {
		parsingResult = false;
	}
	body = "";
	Serial.println(String(F("Free memory after parseBody>> ")) + freeMemory());
	
	return parsingResult;
}

bool ResponseParser::limitBodyToJson() {
	int begPos = body.indexOf('{');
	int endPos = body.lastIndexOf('}');
	if (begPos == -1 || endPos == -1 ) {
		return false;
	} else {
		body = body.substring(begPos, endPos + 1);	
		// Serial.println((String)"body >>\n" + body);	
		// body.trim();
		return true;
	}	
}

bool ResponseParser::findAndSaveData() {
	//TODO analize username, password and boardUid
	int nextElBegPos = 0;
	int id = 0;
	while (nextElBegPos != -1) {
		nextElBegPos = findAndRememberDeviceValueByKey(id, wrapElement(String(DEVICE_ID_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberDeviceValueByKey(id, wrapElement(String(DEVICE_ACK_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberDeviceValueByKey(id, wrapElement(String(DEVICE_ADJUSTABLE_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberDeviceValueByKey(id, wrapElement(String(DEVICE_CONTROL_VALUE_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberDeviceValueByKey(id, wrapElement(String(DEVICE_RADIO_ERROR_KEY)), nextElBegPos);
		// Serial.println((String)"nextElBegPos>> " + nextElBegPos);
		
		if (nextElBegPos != -1) {
			body = body.substring(nextElBegPos);
			nextElBegPos = 0;
			id++;
		} else {
			// can't parse all data
			return false;
		}		
		// Serial.println((String)"body>> " + body);
	}
	return true;
}

int ResponseParser::findAndRememberDeviceValueByKey(uint8_t id, String key, int begPos) {
	if (begPos == -1) return -1;
	
	String value = "";
	int dataEndPos = findAndFetchValueByKey(key, begPos, value);
	
	if (dataEndPos == -1) return -1;
	
	matchAndRememberDeviceElement(id, key, value);
	// Serial.println((String)">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + key + " >> " + value);
	
	return dataEndPos;
}

int ResponseParser::findAndFetchValueByKey(String key, int begPos, String& value) {
	int endBracePos = body.indexOf("}", begPos);
	int keyPos = body.indexOf(key, begPos);	//ERROR
	if (keyPos == -1 || endBracePos < keyPos) return -1;
	
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
	
	if (strcmp(value.c_str(), "") == 0) {
		return -1;
	}
	
	return dataEndPos;
}

void ResponseParser::matchAndRememberDeviceElement(uint8_t id, String key, String value) {
	const char* key_char = key.c_str();
	if (strcmp(key_char, String(DEVICE_ID_KEY).c_str()) == 0) {
		_deviceInfoArray[id] = new DeviceInfo(id);		
	} else if (strcmp(key_char, String(DEVICE_ACK_KEY).c_str()) == 0) {
		float val = atof(value.c_str()); // TODO string is not validated
		_deviceInfoArray[id]->setAck(val);
	} else if (strcmp(key_char, String(DEVICE_ADJUSTABLE_KEY).c_str()) == 0) {
		bool val = strcmp(value.c_str(), "true") == 0 ? true : false;
		_deviceInfoArray[id]->setAdjustable(val);
	} else if (strcmp(key_char, String(DEVICE_CONTROL_VALUE_KEY).c_str()) == 0) {
		float val = atof(value.c_str());
		_deviceInfoArray[id]->setControlVal(val);
	} else if (strcmp(key_char, String(DEVICE_RADIO_ERROR_KEY).c_str()) == 0) {		
		bool val = strcmp(value.c_str(), "true") == 0 ? true : false;
		_deviceInfoArray[id]->setRadioError(val);		
	}
}

bool ResponseParser::findAndAnalizeRegMessage() {
	String value = "";
	if (!findAndFetchValueByKey(wrapElement(String(REGISTRATION_MESSAGE_KEY)), 0, value)) {
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