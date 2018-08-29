// ResponseParser.cpp
// (c) Ivanov Aleksandr, 2018

#include "ResponseParser.h"

ResponseParser::ResponseParser(DataBase* dataBasePointer)
{
	init(dataBasePointer);
	Serial.println("ResponseParser()!"); //TEST
}

ResponseParser::~ResponseParser(){
	resetResponse();
}

void ResponseParser::init(DataBase* dataBasePointer){
	_dataBase = dataBasePointer;
	_deviceInfoArray = new DataBase::DeviceInfo[_dataBase->getDeviceCount()];
}

/****************** 
* public interface*
*******************/

void ResponseParser::resetResponse() {
	response = "";
	headers = "";
	body = "";
	delete[] _deviceInfoArray;
}

bool ResponseParser::parseResponse() {
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
		bool result = parseBody();
		if (reault == true) {
			saveData();
			Serial.println(F("<<<<<<<<<<<<Good saving of new data>>>>>>>>>>>>"));
		}
		Serial.println((String)"Free memory jsonBuffer>> " + freeMemory());
		return result;
	} else {
		return false;
	}
}

/********************
* private
********************/

bool ResponseParser::parseHeaders() {
	headers = "";
	return true;
}

bool ResponseParser::parseBody() {
	bool parsingResult;
	if(limitBodyToJson()) {
		parsingResult = findAndSaveData();
	} else {
		parsingResult = false;
	}
	body = "";
	Serial.println((String(F("Free memory after parseBody>> ")) + freeMemory());
	
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
	}	
}

bool ResponseParser::findAndSaveData() {
	int nextElBegPos = 0;
	int id = 0;
	while (nextElBegPos != -1) {
		nextElBegPos = findAndRememberElementByKey(id, wrapElement(String(DEVICE_ID_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberElementByKey(id, wrapElement(String(DEVICE_ACK_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberElementByKey(id, wrapElement(String(DEVICE_ADJUSTABLE_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberElementByKey(id, wrapElement(String(DEVICE_CONTROL_VALUE_KEY)), nextElBegPos);
		nextElBegPos = findAndRememberElementByKey(id, wrapElement(String(DEVICE_RADIO_ERROR_KEY)), nextElBegPos);
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
}

int ResponseParser::findAndRememberElementByKey(int id, String key, int begPos) {
	if (begPos == -1) return -1;
	
	int keyPos = body.indexOf(key, begPos);
	
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
	
	String value = body.substring(dataBegPos, dataEndPos);
	value.trim();
	
	matchAndRememberElement(id, key, value);
	// Serial.println((String)">>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + key + " >> " + value);
	
	return dataEndPos;
}

void ResponseParser::matchAndRememberElement(int id, String key, String value) {
	if (strcmp(key, String(DEVICE_ID_KEY)) == 0) {
		_deviceInfoArray[id] = new DataBase::DeviceInfo((char) id);		
	} else if (strcmp(key, String(DEVICE_ACK_KEY)) == 0) {
		float val = atof(value);
		_deviceInfoArray[id]->setAck(val);
	} else if (strcmp(key, String(DEVICE_ADJUSTABLE_KEY)) == 0) {
		bool val = strcmp(value, "true") == 0 ? true : false;
		_deviceInfoArray[id]->setAdjustable(val);
	} else if (strcmp(key, String(DEVICE_CONTROL_VALUE_KEY)) == 0) {
		float val = atof(value);
		_deviceInfoArray[id]->setControlVal(val);
	} else if (strcmp(key, String(DEVICE_RADIO_ERROR_KEY)) == 0) {		
		bool val = strcmp(value, "true") == 0 ? true : false;
		_deviceInfoArray[id]->setRadioError(val);		
	}
}

void ResponseParser::saveData() {
	for (uint8_t i=0; i < _dataBase->getDeviceCount(); i++) {
		_dataBase->setDeviceAck((char) i, _deviceInfoArray[id]->getAck());
		_dataBase->setDeviceAdj((char) i, _deviceInfoArray[id]->getAdjustable());
		_dataBase->setDeviceControlValue((char) i, _deviceInfoArray[id]->getControlVal());
		_dataBase->setDeviceRFErr((char) i, _deviceInfoArray[id]->hasRadioError());
	}
}

String ResponseParser::wrapElement(String key) {
	return String(F("\"")) + key + F("\"");
}