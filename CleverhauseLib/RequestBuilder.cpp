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

// target /cleverhause/arduino/data
String RequestBuilder::buildRequest(String host, String port, String target, String SSID, String password, String boardUID) {
	String requestBody = formRequestBody(SSID, password, boardUID); // should do so for calculation of body length
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

String RequestBuilder::formRequestBody(String SSID, String password, String boardUID) {
	// String requestBody = F("{\"devicesState\": [],\"controllerErrors\": {\"gsmError\": \"false\",\"lcdError\" : \"false\",\"radioError\" : \"false\"}}"); // STUB
	String requestJson = String("{");
	//DEVICE STATE BLOK
	requestJson += String("") + F("\"") + String(DEVICES_STATES_BLOCK_NAME) + F("\"") + F(":");
	requestJson += F("[");
	
	int deviceCount = _dataBase->getDeviceCount();
	
	if (deviceCount) {
		uint8_t* deviceIds = new uint8_t[deviceCount];
		_dataBase->fetchIds(deviceIds);
		for(int i = 0; i < deviceCount - 1; i++) {
			requestJson += buildRequestDeviceJson((char) deviceIds[i]);
			requestJson += F(",");
		}
		requestJson += buildRequestDeviceJson(deviceIds[deviceCount - 1]);
		delete[] deviceIds;
	}	
	requestJson += F("]");
	requestJson += F(",");
	// GLOBAL ERRORS BLOCK
	requestJson += String(F("")) + F("\"") + String(GLOBAL_ERRORS_BLOCK_NAME) + F("\"") + F(":");
	requestJson += buildRequestGlobalErrorsJson();
	requestJson += F("}");
	
	_bodyLength = requestJson.length();
	
	return requestJson;
}

String RequestBuilder::buildRequestDeviceJson(char id) {
	float ack = _dataBase->getDeviceAck(id);
	bool adjustable = _dataBase->getDeviceAdj(id);
	float controlValue = _dataBase->getDeviceControlValue(id);
	bool radioError = _dataBase->getDeviceRFErr(id);
	
	String deviceJsonString = String("{");
	deviceJsonString += String("") + F("\"") + String(DEVICE_ID_KEY_NAME) + F("\"") + F(":");
	deviceJsonString += (uint8_t) id;
	deviceJsonString += F(",");
	deviceJsonString += String("") + F("\"") + String(DEVICE_ACK_KEY_NAME) + F("\"") + F(":");
	deviceJsonString += ack; //TODO format
	deviceJsonString += F(",");
	deviceJsonString += String(F("")) + F("\"") + String(DEVICE_ADJUSTABLE_KEY_NAME) + F("\"") + F(":");
	deviceJsonString += adjustable ? F("true") : F("false");
	deviceJsonString += F(",");
	deviceJsonString += String("") + F("\"") + String(DEVICE_CONTROL_VALUE_KEY_NAME) + F("\"") + F(":");
	deviceJsonString += controlValue; // TODO format
	deviceJsonString += F(",");
	deviceJsonString += String("") + F("\"") + String(DEVICE_RADIO_ERROR_KEY_NAME) + F("\"") + F(":");
	deviceJsonString += radioError ? F("true") : F("false");
	deviceJsonString += F("}");
	
	return deviceJsonString;
}

String RequestBuilder::buildRequestGlobalErrorsJson() {
	bool gsmError = _dataBase->getGsmError();
	bool globalRadioError = _dataBase->getRadioError();
	bool lcdError = _dataBase->getLcdError();
	
	String globalErrorsJsonString = String(F("{"));
	globalErrorsJsonString += jsonKeyWrapper(String(GSM_ERROR_KEY_NAME));
	// globalErrorsJsonString += String(F("")) + F("\"") + String(GSM_ERROR_KEY_NAME) + F("\"") + F(":");
	globalErrorsJsonString += gsmError ? F("true") : F("false");
	globalErrorsJsonString += F(",");
	globalErrorsJsonString += String(F("")) + F("\"") + String(RADIO_ERROR_KEY_NAME) + F("\"") + F(":");
	globalErrorsJsonString += globalRadioError ? F("true") : F("false");
	globalErrorsJsonString += F(",");
	globalErrorsJsonString += String(F("")) + F("\"") + String(LCD_ERROR_KEY_NAME) + F("\"") + F(":");
	globalErrorsJsonString += lcdError ? F("true") : F("false");	
	globalErrorsJsonString += F("}");
	
	return globalErrorsJsonString;
}

String RequestBuilder::jsonKeyWrapper(String key) {
	return String(F("\"")) + key + F("\"") + F(":");
}