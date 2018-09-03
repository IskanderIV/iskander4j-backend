// ResponseParser.h
// (c) Ivanov Aleksandr, 2018

#ifndef _ResponseParser_H_
#define _ResponseParser_H_

#include "DataBase.h"
#include "GlobalResponse.h"
#include "HttpExchangeType.h"
#include "HttpJsonKeys.h"

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