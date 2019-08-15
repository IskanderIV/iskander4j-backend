// ResponseParser.h
// (c) Ivanov Aleksandr, 2018

#ifndef _ResponseParser_H_
#define _ResponseParser_H_

#define HTTP "HTTP"
#define EMPT_LINE "\r\n\r\n"
#define END_OF_LINE "\r\n"
#define OK200 "200"

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
	int _maxExpectedDevices;
	
	void init(DataBase* pDataBasePointer);	
	
	bool splitResponse(String& headers, String& body);
	int parseHeaders(String& headers);
	bool parseHeader();
	bool parseBody(HttpExchangeType type, String& body);
	bool trimBodyToJson(String& body);
	bool findAndSaveData(String& body);
	int findAndRememberDeviceValueByKey(String& body, uint8_t id, String key, int begPos);
	int findAndFetchValueByKey(String& body, String key, int begPos, String& value);
	bool matchAndRememberDeviceElement(uint8_t id, String key, String& value);
	bool findAndAnalizeRegMessage(String& body);
	String wrapElement(String key);
};

#endif