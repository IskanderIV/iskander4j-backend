// ResponseParser.h
// (c) Ivanov Aleksandr, 2018

#ifndef _ResponseParser_H_
#define _ResponseParser_H_

#include "DataBase.h"
#include "GlobalResponse.h"
#include "HttpExchangeType.h"
#include "HttpJsonKeys.h"

class DataBase;
// class DataBase::DeviceInfo;
typedef DataBase::DeviceInfo DeviceInfo;

class ResponseParser
{
public:
	ResponseParser(DataBase* pDataBasePointer);
	~ResponseParser();
	
	// public interfaces
	void resetResponse();
	bool parseResponse(HttpExchangeType type);
	void saveData();
	
private:
	DataBase* _dataBase;
	DeviceInfo** _deviceInfoArray;
	
	bool _goodParsing;
	
	void init(DataBase* pDataBasePointer);	
	
	bool parseHeaders();
	bool parseHeader();
	bool parseBody(HttpExchangeType type);
	bool limitBodyToJson();
	bool findAndSaveData();
	int findAndRememberDeviceValueByKey(uint8_t id, String key, int begPos);
	int findAndFetchValueByKey(String key, int begPos, String& value);
	void matchAndRememberDeviceElement(uint8_t id, String key, String value);
	bool findAndAnalizeRegMessage();
	String wrapElement(String key);
};

#endif