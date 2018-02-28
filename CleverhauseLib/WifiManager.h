// WifiManager
// (c) Ivanov Aleksandr, 2018

#ifndef _WifiManager_H_
#define _WifiManager_H_

#define ESP_RX 10
#define ESP_TX 9
//#define SERVER_IP "192.168.1.34"

//REQUEST
#define END_OF_STRING "\r\n"
#define FINDED_WAN_DELIMITER "\n"
#define MAX_FINDED_WAN_COUNT 8

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

//RESPONSE

#include "Object.h"
//#include <SoftwareSerial.h>
#include <ESP8266pro.h>
#include <ESP8266proClient.h>
/*"devices"*/
//#define DEBUG

//class SoftwareSerial;
class ESP8266pro;
class ESP8266proClient;
class ESP8266proConnection;
class EepromManager;
class DataBase;

extern void parseHttpResponse(ESP8266proConnection* connection,
                   char* buffer, int length, boolean completed);

class WifiManager : public Object
{
public:
	WifiManager(int _freq);
	~WifiManager();
	
	// interface impl for controllers events
	bool connectToWifi();
	bool executePutRequest();
	void closeConnection();
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
	String _serverAdress;
	bool _wifiError;
	bool _noTcpConnection;
	bool _cantSend;
	bool _noRessponse;
	bool _cantClose;
	long _serverPort;
	int _findedWANsCount;
	
	void initWifi(int _freq);
	void initTcpConnection();
	// void parseHttpResponse(ESP8266proConnection* connection,
                   // char* buffer, int length, boolean completed);
	String buildRequest();
	String formRequestJson();
	String buildRequestDeviceJson(char id);
	String buildRequestGlobalErrorsJson();
};

#endif