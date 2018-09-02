// ResponseParser.h
// (c) Ivanov Aleksandr, 2018

#ifndef _ResponseParser_H_
#define _ResponseParser_H_

#define DEVICES_STATES_BLOCK_KEY 		"devices"

#define USER_NAME		 				"username"
#define PASSWORD		 				"password"
#define BOARD_UID		 				"boardUID"

#define DEVICE_ID_KEY	 				"id"
#define DEVICE_ACK_KEY		 			"ack"
#define DEVICE_ADJUSTABLE_KEY	 		"adj"
#define DEVICE_CONTROL_VALUE_KEY	 	"ctrlVal"
#define DEVICE_RADIO_ERROR_KEY		 	"radioErr"

#define GLOBAL_ERRORS_BLOCK_KEY	 	"errors"
#define REGISTRATION_MESSAGE_KEY	"message"

#define GSM_ERROR_KEY 				"gsm"
#define RADIO_ERROR_KEY 			"radio"
#define LCD_ERROR_KEY 				"lcd"

#include "DataBase.h"
#include "GlobalResponse.h"
#include "HttpExchangeType.h"

class DataBase;
class DataBase::DeviceInfo;

class ResponseParser
{
public:
	ResponseParser();
	~ResponseParser();
	
	// public interfaces
	void resetResponse();
	bool parseResponse(HttpExchangeType type);
	void saveData();
	
private:
	DataBase* _dataBase;
	DataBase::DeviceInfo* _deviceInfoArray;
	
	bool _goodParsing;
	
	void init();	
	
	bool parseHeaders();
	bool parseHeader();
	bool parseBody(HttpExchangeType type);
	bool limitBodyToJson();
	bool findAndSaveData();
	int findAndRememberDeviceValueByKey(int id, String key, int begPos);
	bool findAndFetchValueByKey(String key, String& value);
	void matchAndRememberDeviceElement(int id, String key, String value);
	bool findAndAnalizeRegMessage();
	String wrapElement(String key);
};

#endif