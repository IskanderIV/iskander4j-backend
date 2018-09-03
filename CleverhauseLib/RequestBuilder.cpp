// RequestBuilder.cpp
// (c) Ivanov Aleksandr, 2018

#include "RequestBuilder.h"

RequestBuilder::RequestBuilder(DataBase* dataBasePointer)
{
	init(dataBasePointer);
	Serial.println("RequestBuilder()!");//TEST
}

RequestBuilder::~RequestBuilder(){
	
}

void RequestBuilder::init(DataBase* dataBasePointer){
	_bodyLength = 0;
	_dataBase = dataBasePointer;
}

/****************** 
* public interface*
*******************/

String RequestBuilder::buildRequest(HttpExchangeType type, String host, String port, String target, String SSID, String password, String boardUID) {
	// should do this before headers for calculation of body length
	String requestBody = formRequestBody(type, SSID, password, boardUID);
	
	String request = formRequestHeaders(host, port, target) + requestBody; 
	
	return request;
}

/********************
* private
********************/

String RequestBuilder::formRequestHeaders(String host, String port, String target) {
	String headers = String(F("")); 
	headers += F("PUT ") + target + F(" HTTP/1.1"); // TODO target - should be in memory
	headers += END_OF_STRING;
	headers += F("Host: ");
	headers += host + F(":");
	headers += port;
	headers += END_OF_STRING;	
	headers += F("Content-Length: ");
	headers += _bodyLength.length(); // TODO from arguments or class fields
	headers += END_OF_STRING;
	headers += F("Content-Type: application/json");
	headers += END_OF_STRING;
	headers += END_OF_STRING;
	
	return headers;
}

String RequestBuilder::formRequestBody(HttpExchangeType type, String login, String password, String boardUID) {
	
	String requestJson = String("{"); // begin curly brace
	// username
	requestJson += jsonKeyWrapper(String(USER_NAME_KEY));
	requestJson += jsonStringValueWrapper(login);
	// password
	requestJson += jsonKeyWrapper(String(PASSWORD_KEY));
	requestJson += jsonStringValueWrapper(password);
	// boardUID
	requestJson += jsonKeyWrapper(String(BOARD_UID_KEY));
	requestJson += jsonStringValueWrapper(boardUID);
	
	//DEVICE STATE BLOK
	buildDevicesBlockJson(type);
	requestJson += F(",");
	
	// GLOBAL ERRORS BLOCK
	requestJson += jsonKeyWrapper(String(GLOBAL_ERRORS_BLOCK_KEY));
	requestJson += buildRequestGlobalErrorsJson();
	
	requestJson += F("}"); // end curly brace
	
	_bodyLength = requestJson.length();
	
	return requestJson;
}

String RequestBuilder::buildDevicesBlockJson(HttpExchangeType type) {
	requestJson += jsonKeyWrapper(String(DEVICES_STATES_BLOCK_KEY));
	requestJson += F("["); // end devices array brace
	// the better thing is to form list of data info objects and pass here
	int deviceCount = _dataBase->getDeviceCount();
	
	if (deviceCount) {
		uint8_t* deviceIds = new uint8_t[deviceCount];
		_dataBase->fetchIds(deviceIds);
		for(int i = 0; i < deviceCount; i++) {
			switch (type) {
				case DATA: 
					requestJson += buildDeviceDataJson((char) deviceIds[i]); break;
				case REG:
					requestJson += buildDeviceRegistratonJson((char) deviceIds[i]); break;
			}
			
			if (i < deviceCount - 1) {
				requestJson += F(",");
			}			
		}
		delete[] deviceIds;
	}	
	requestJson += F("]"); // end devices array brace
}

String RequestBuilder::buildDeviceDataJson(char id) {
	float ack = _dataBase->getDeviceAck(id);
	bool adjustable = _dataBase->getDeviceAdj(id);
	float controlValue = _dataBase->getDeviceControlValue(id);
	bool radioError = _dataBase->getDeviceRFErr(id);
	
	String deviceJsonString = String("{");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ID_KEY));
	deviceJsonString += (uint8_t) id;
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
	
	return deviceJsonString;
}

String RequestBuilder::buildDeviceRegistratonJson(char id) {	
	float min = 0.0; //STUB
	float max = 1.0; //STUB
	float discrete = 1.0; //STUB
	bool adjustable = _dataBase->getDeviceAdj(id);
	bool rotate = false; //STUB
	bool signaling = false; //STUB
	
	String deviceJsonString = String("{");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_ID_KEY_NAME));
	deviceJsonString += (uint8_t) id;
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_MIN_KEY));
	deviceJsonString += min; //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_MAX_KEY));
	deviceJsonString += max; //TODO format
	deviceJsonString += F(",");
	deviceJsonString += jsonKeyWrapper(String(DEVICE_REG_DISCRETE_KEY));
	deviceJsonString += discrete; //TODO format
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
	
	return globalErrorsJsonString;
}

String RequestBuilder::jsonKeyWrapper(String key) {
	return String(F("\"")) + key + F("\"") + F(":");
}

String RequestBuilder::jsonBoolValueWrapper(bool val) {
	return val ? F("true") : F("false");
}

String RequestBuilder::jsonStringValueWrapper(String val) {
	return String(F("\"")) + val + F("\"");
}