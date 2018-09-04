// RequestBuilder
// (c) Ivanov Aleksandr, 2018

#ifndef _RequestBuilder_H_
#define _RequestBuilder_H_

#define END_OF_STRING "\r\n"

#include "DataBase.h"
#include "HttpExchangeType.h"
#include "HttpJsonKeys.h"

// enum HttpExchangeType {
	// DATA,
	// REG
// };

class DataBase;

class RequestBuilder
{
public:
	RequestBuilder(DataBase* pDataBasePointer);
	~RequestBuilder();
	
	// public interfaces
	String buildRequest(HttpExchangeType type, String host, String port, String target, String SSID, String password, String boardUID);
	
private:
	DataBase* _dataBase;
	int _bodyLength;
	void init(DataBase* pDataBasePointer);
	String formRequestHeaders(String host, String port, String target);
	String formRequestBody(HttpExchangeType type, String SSID, String password, String boardUID);
	String buildDevicesBlockJson(HttpExchangeType type);
	String buildDeviceDataJson(uint8_t id);
	String buildDeviceRegistratonJson(uint8_t id);
	String buildRequestGlobalErrorsJson();
	String jsonKeyWrapper(String key);
	String jsonBoolValueWrapper(bool val);
	String jsonStringValueWrapper(String val);
};

#endif