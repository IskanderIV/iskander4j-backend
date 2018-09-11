// WifiManager
// (c) Ivanov Aleksandr, 2018

#ifndef _WifiManager_H_
#define _WifiManager_H_

#define ESP_RX 10
#define ESP_TX 9
//#define SERVER_IP "192.168.1.34"

#define FINDED_WAN_DELIMITER "\n"
#define MAX_FINDED_WAN_COUNT 8

#include "Object.h"
#include "RequestBuilder.h"
#include "ResponseParser.h"
#include "RequestBuilder2.h"
#include "ResponseParser2.h"
#include "HttpExchangeType.h"
#include <ESP8266pro.h>
#include <ESP8266proClient.h>
//#define DEBUG

//class SoftwareSerial;
class ESP8266pro;
class ESP8266proClient;
class ESP8266proConnection;
class EepromManager;
class DataBase;
class RequestBuilder2;
class ResponseParser2;


class WifiManager : public Object
{
public:
	WifiManager(int _freq, DataBase* pDataBase);
	~WifiManager();
	
	static void saveHttpResponse(ESP8266proConnection* connection, char* buffer, int length, boolean completed);
	
	// public interface
	bool executeRequest(HttpExchangeType type);
	void fetchFindedWANs(String*);
	int getFindedWANsCount();
	int getMaxFindedWANsCount();
	String getFindedWANsDelimiter();
	void setDataBase(DataBase* _dataBase);
	bool connectToWifi();
	void closeConnection();
	
private:
	//SoftwareSerial espSerial;
	ESP8266pro _wifi;
	DataBase* _dataBase;
	RequestBuilder2* _requestBuilder2;
	RequestBuilder* _requestBuilder;
	ResponseParser* _responseParser;
	ResponseParser2* _responseParser2;
	char* _request2;
	char* _response2;
	
	String _SSID;
	String _ssidPassword;
	
	String _host;
	String _port;
	String _target;

	String _login;
	String _password;
	
	String _boardUID;
	
	String _serverAdress;
	bool _wifiError;
	bool _noTcpConnection;
	bool _cantSend;
	bool _noRessponse;
	bool _cantClose;
	long _serverPort;
	int _findedWANsCount;
	
	void init(int _freq, DataBase* pDataBase);
	void initWifi(int _freq);
	void initTcpConnection();
	void prepareAndSendRequest(HttpExchangeType type, ESP8266proClient* con);
	void parseHttpResponse(HttpExchangeType type, ESP8266proClient* con);
};

#endif