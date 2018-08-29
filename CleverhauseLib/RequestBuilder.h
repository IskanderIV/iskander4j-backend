// RequestBuilder
// (c) Ivanov Aleksandr, 2018

#ifndef _RequestBuilder_H_
#define _RequestBuilder_H_

#define END_OF_STRING "\r\n"

#define DEVICES_STATES_BLOCK_NAME 		"devicesState"

#define DEVICE_ID_KEY_NAME 				"id"
#define DEVICE_ACK_KEY_NAME 			"ack"
#define DEVICE_ADJUSTABLE_KEY_NAME 		"adj"
#define DEVICE_CONTROL_VALUE_KEY_NAME 	"ctrlVal"
#define DEVICE_RADIO_ERROR_KEY_NAME 	"radioErr"

#define GLOBAL_ERRORS_BLOCK_NAME 		"errors"

#define GSM_ERROR_KEY_NAME 				"gsm"
#define RADIO_ERROR_KEY_NAME 			"radio"
#define LCD_ERROR_KEY_NAME 				"lcd"

#include "DataBase.h"

class DataBase;

class RequestBuilder
{
public:
	RequestBuilder();
	~RequestBuilder();
	
	// public interfaces
	String buildRequest(String host, String port, String target, String SSID, String password, String boardUID);
	
private:
	DataBase* _dataBase;
	int _bodyLength;
	void init();
	String buildRequest(String host, String port, String target, String SSID, String password, String boardUID);
	String formRequestHeaders(String host, String port, String target);
	String formRequestBody(String SSID, String password, String boardUID);
	String buildRequestDeviceJson(char id);
	String buildRequestGlobalErrorsJson();
	String jsonKeyWrapper(String key);
};

#endif