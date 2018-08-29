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
//#include <SoftwareSerial.h>
#include <ESP8266pro.h>
#include <ESP8266proClient.h>
//#define DEBUG

//class SoftwareSerial;
class ESP8266pro;
class ESP8266proClient;
class ESP8266proConnection;
class EepromManager;
class DataBase;


class WifiManager : public Object
{
public:
	WifiManager(int _freq);
	~WifiManager();
	
	// interface

	bool executePutRequest();
	void fetchFindedWANs(String*);
	int getFindedWANsCount();
	int getMaxFindedWANsCount();
	String getFindedWANsDelimiter();
	void setDataBase(DataBase* _dataBase);
	void setEepromManager(EepromManager* _eepromMngr);
	
private:
	//SoftwareSerial espSerial;
	ESP8266pro _wifi;
	DataBase* _dataBase;
	EepromManager* _eepromMngr;
	RequestBuilder* _responseBuilder;
	ResponseParser* _responseParser;
	
	String _host;
	String _port;
	String _target:
	String _SSID;
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
	
	void init(int _freq);
	void initWifi(int _freq);
	void initTcpConnection();
	
	bool connectToWifi();
	void closeConnection();
};

#endif