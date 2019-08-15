// RequestBuilder.cpp
// (c) Ivanov Aleksandr, 2018

#include "RequestBuilder.h"
#include <MemoryFree.h>

RequestBuilder::RequestBuilder(DataBase* pDataBasePointer)
{
	init(pDataBasePointer);
	Serial.println("RequestBuilder()!");//TEST
}

RequestBuilder::~RequestBuilder(){
	
}

void RequestBuilder::init(DataBase* pDataBasePointer){
	_bodyLength = 0;
	_dataBase = pDataBasePointer;
}

/****************** 
* public interface*
*******************/

String RequestBuilder::buildRequest(HttpExchangeType type, String& host, String& port, String& SSID, String& password, String& boardUID) {
	// should do this before headers for calculation of body length
	String requestBody = formRequestBody(type, SSID, password, boardUID);
	// Serial.println(String(F("requestBody:\n")) + requestBody);//TEST
	Serial.println((String)F("Free memory before header + body operation >> ") + freeMemory());//TEST
	String request = formRequestHeaders(type, host, port) + requestBody;
	requestBody = "";
	Serial.println((String)F("Free memory end of buildRequest>> ") + freeMemory());//TEST
	return request;
}

/********************
* private
********************/

String RequestBuilder::formRequestHeaders(HttpExchangeType type, String& host, String& port) {
	String headers = String(F("")); 
	String target;
	if (type == DATA) {
		target = _dataBase->getTarget();
	} else if (type == REG) {
		target = F("/cleverhause/boards/board/registration");
	}
	
	headers += (String)F("POST ") + target + F(" HTTP/1.1"); // TODO PUT for AWS
	headers += END_OF_STRING;
	headers += F("Host: ");
	headers += host + F(":");
	headers += port;
	headers += END_OF_STRING;	
	headers += F("Content-Length: ");
	// Serial.println(String(F("headers:\n")) + headers);//TEST
	Serial.println((String)F("Free memory headers Content-Length>> ") + freeMemory());//TEST
	headers += String(_bodyLength);
	headers += END_OF_STRING;
	headers += F("Content-Type: application/json");
	headers += END_OF_STRING;
	headers += END_OF_STRING;
	// Serial.println(String(F("headers:\n")) + headers);//TEST
	Serial.println((String)F("Free memory after headers>> ") + freeMemory());//TEST
	return headers;
}

String RequestBuilder::formRequestBody(HttpExchangeType type, String& login, String& password, String& boardUID) {
	
	String requestJson = String("{"); // begin curly brace
	// username
	requestJson += jsonKeyWrapper(String(USER_NAME_KEY));
	requestJson += jsonStringValueWrapper(login);
	requestJson += F(",");
	// password
	requestJson += jsonKeyWrapper(String(PASSWORD_KEY));
	requestJson += jsonStringValueWrapper(password);
	requestJson += F(",");
	// boardUID
	requestJson += jsonKeyWrapper(String(BOARD_UID_KEY));
	requestJson += jsonStringValueWrapper(boardUID);
	requestJson += F(",");
	// Serial.println(String(F("Part of REQUEST:\n")) + requestJson);//TEST
	//DEVICE STATE BLOK
	requestJson += buildDevicesBlockJson(type);
	requestJson += F(",");
	// Serial.println(String(F("after \"devices\"....."))); //TEST
	_bodyLength = requestJson.length();//TEST
	// Serial.println(String(F("_bodyLength.....")) + _bodyLength); //TEST
	// GLOBAL ERRORS BLOCK
	requestJson += jsonKeyWrapper(String(GLOBAL_ERRORS_BLOCK_KEY));
	requestJson += buildRequestGlobalErrorsJson();
	
	requestJson += F("}"); // end curly brace
	requestJson += END_OF_STRING;
	// Serial.println(String(F("requestJson....."))); //TEST
	_bodyLength = requestJson.length();
	// Serial.println(String(F("_bodyLength.....")) + _bodyLength); //TEST
	Serial.println((String)F("Free memory after formRequestBody>> ") + freeMemory());//TEST
	return requestJson;
}

String RequestBuilder::buildDevicesBlockJson(HttpExchangeType type) {
	String devicesJson = "";
	devicesJson += jsonKeyWrapper(String(DEVICES_STATES_BLOCK_KEY));
	devicesJson += F("["); // begin of devices array brace
	// the better thing is to form list of data info objects and pass here
	int deviceCount = _dataBase->getDeviceCount();
	
	if (deviceCount) {
		const int maxDevices = _dataBase->getMaxDevices();
		uint8_t deviceIds[maxDevices];
		memset(deviceIds, 0, maxDevices);
		_dataBase->fetchIds(deviceIds);
		for(uint8_t i = 0; i < deviceCount; i++) {
			Serial.println(String(F("deviceIds[")) + i + F("]") + deviceIds[i]);//TEST
			
			switch (type) {
				case DATA: 
					devicesJson += buildDeviceDataJson(deviceIds[i]); break;
				case REG:
					devicesJson += buildDeviceRegistratonJson(deviceIds[i]); break;
			}
			Serial.println((String)F("Free memory buildDevicesBlockJson>> ") + freeMemory());//TEST
			if (i < deviceCount - 1) {
				devicesJson += F(",");
			}			
		}
	}	
	devicesJson += F("]"); // end of devices array brace
	Serial.println((String)F("Free memory after alldeviceIds[i]>> ") + freeMemory());//TEST
	return devicesJson;
}

String RequestBuilder::buildDeviceDataJson(uint8_t id) {
	float ack = _dataBase->getDeviceAck(id);
	bool adjustable = _dataBase->getDeviceAdj(id);
	float controlValue = _dataBase->getDeviceControlValue(id);
	bool radioError = _dataBase->getDeviceRFErr(id);
	
	String deviceJsonString = String("{");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ID_KEY));
	deviceJsonString += id;
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ACK_KEY));
	deviceJsonString += ack; //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ADJUSTABLE_KEY));
	deviceJsonString += jsonBoolValueWrapper(adjustable);
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_CONTROL_VALUE_KEY));
	deviceJsonString += controlValue; // TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_RADIO_ERROR_KEY));
	deviceJsonString += jsonBoolValueWrapper(radioError);
	deviceJsonString += F("}");
	// Serial.println(String(F("deviceJsonString:\n")) + deviceJsonString);//TEST
	Serial.println((String)"Free memory >> " + freeMemory());//TEST
	return deviceJsonString;
}

String RequestBuilder::buildDeviceRegistratonJson(uint8_t id) {	
	float min = _dataBase->getDeviceMin(id);
	float max = _dataBase->getDeviceMax(id);
	float discrete = _dataBase->getDeviceDiscrete(id);
	bool adjustable = _dataBase->getDeviceAdj(id);
	bool rotate = _dataBase->getDeviceRotatable(id);
	bool signaling = true; // STUB not need me in future
	
	String deviceJsonString = String("{");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ID_KEY));
	deviceJsonString += String(id);
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_MIN_KEY));
	deviceJsonString += String(min); //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_MAX_KEY));
	deviceJsonString += String(max); //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_DISCRETE_KEY));
	deviceJsonString += String(discrete); //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ADJUSTABLE_KEY));
	deviceJsonString += jsonBoolValueWrapper(adjustable);
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_ROTATE_KEY));
	deviceJsonString += jsonBoolValueWrapper(rotate);
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_SIGNALING_KEY));
	deviceJsonString += jsonBoolValueWrapper(signaling);
	deviceJsonString += F("}");
	
	return deviceJsonString;
}

String RequestBuilder::buildRequestGlobalErrorsJson() {
	bool gsmError = _dataBase->getGsmError();
	bool globalRadioError = _dataBase->getRadioError();
	bool lcdError = _dataBase->getLcdError();
	
	String globalErrorsJsonString = String(F("{"));
	globalErrorsJsonString += jsonKeyWrapper(String(GSM_ERROR_KEY));
	globalErrorsJsonString += jsonBoolValueWrapper(gsmError);
	globalErrorsJsonString += F(",");
	globalErrorsJsonString += jsonKeyWrapper(String(RADIO_ERROR_KEY));
	globalErrorsJsonString += jsonBoolValueWrapper(globalRadioError);
	globalErrorsJsonString += F(",");
	globalErrorsJsonString += jsonKeyWrapper(String(LCD_ERROR_KEY));
	globalErrorsJsonString += jsonBoolValueWrapper(lcdError);	
	globalErrorsJsonString += F("}");
	// Serial.println(String(F("globalErrorsJsonString:\n")) + globalErrorsJsonString);//TEST
	Serial.println((String)F("Free memory >> ") + freeMemory());//TEST
	return globalErrorsJsonString;
}

String RequestBuilder::jsonKeyWrapper(String key) {
	return String(F("\"")) + key + F("\"") + F(":");
}

String RequestBuilder::jsonBoolValueWrapper(bool val) {
	return val ? F("true") : F("false");
}

String RequestBuilder::jsonStringValueWrapper(String& val) {
	return String(F("\"")) + val + F("\"");
}