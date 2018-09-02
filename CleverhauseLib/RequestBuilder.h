// RequestBuilder
// (c) Ivanov Aleksandr, 2018

#ifndef _RequestBuilder_H_
#define _RequestBuilder_H_

#define END_OF_STRING "\r\n"

#define DEVICES_STATES_BLOCK_NAME 		"devices"

#define USER_NAME		 				"username"
#define PASSWORD		 				"password"
#define BOARD_UID		 				"boardUID"

#define DEVICE_ID_KEY_NAME 				"id"
#define DEVICE_ACK_KEY_NAME 			"ack"
#define DEVICE_ADJUSTABLE_KEY	 		"adj"
#define DEVICE_CONTROL_VALUE_KEY_NAME 	"ctrlVal"
#define DEVICE_RADIO_ERROR_KEY_NAME 	"radioErr"

#define DEVICE_REG_MIN_KEY 				"min"
#define DEVICE_REG_MAX_KEY 				"max"
#define DEVICE_REG_DISCRETE_KEY 		"discrete"
#define DEVICE_REG_ROTATE_KEY 			"rotate"
#define DEVICE_REG_SSIGNLING_KEY		"signaling"

#define GLOBAL_ERRORS_BLOCK_NAME 		"errors"

#define GSM_ERROR_KEY_NAME 				"gsm"
#define RADIO_ERROR_KEY_NAME 			"radio"
#define LCD_ERROR_KEY_NAME 				"lcd"

#include "DataBase.h"
#include "HttpExchangeType.h"

// enum HttpExchangeType {
	// DATA,
	// REG
// };

class DataBase;

class RequestBuilder
{
public:
	RequestBuilder();
	~RequestBuilder();
	
	// public interfaces
	String buildDataRequest(String host, String port, String target, String SSID, String password, String boardUID);
	String buildRegistrationRequest(String host, String port, String target, String SSID, String password, String boardUID);
	
private:
	DataBase* _dataBase;
	int _bodyLength;
	void init();
	String buildRequest(HttpExchangeType type, String host, String port, String target, String SSID, String password, String boardUID);
	String formRequestHeaders(String host, String port, String target);
	String formRequestBody(HttpExchangeType type, String SSID, String password, String boardUID);
	String buildDevicesBlockJson(HttpExchangeType type);
	String buildDeviceDataJson(char id);
	String buildDeviceRegistratonJson(char id);
	String buildRequestGlobalErrorsJson();
	String jsonKeyWrapper(String key);
	String jsonBoolValueWrapper(bool val);
	String jsonStringValueWrapper(String val);
};

#endif