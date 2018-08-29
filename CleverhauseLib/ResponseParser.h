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

#define GSM_ERROR_KEY 				"gsm"
#define RADIO_ERROR_KEY 			"radio"
#define LCD_ERROR_KEY 				"lcd"

#include "DataBase.h"
#include "GlobalResponse.h"

class DataBase;
class DataBase::DeviceInfo;

class ResponseParser
{
public:
	ResponseParser();
	~ResponseParser();
	
	// public interfaces
	void resetResponse();
	bool parseResponse();
	
private:
	DataBase* _dataBase;
	DataBase::DeviceInfo* _deviceInfoArray;
	
	void init();	
	
	bool parseHeaders();
	bool parseHeader();
	bool parseBody();
	void saveData();
	bool limitBodyToJson();
	bool findAndSaveData();
	int findAndRememberElementByKey(String key, int begPos);
	String wrapElement(String key);
};

#endif