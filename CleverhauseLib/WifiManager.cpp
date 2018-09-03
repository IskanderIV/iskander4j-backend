// WifiManager
// (c) Ivanov Aleksandr, 2018

#include "WifiManager.h"
#include "EepromManager.h"
#include "DataBase.h"
#include <MemoryFree.h>


WifiManager::WifiManager(int pFreq, DataBase* pDataBase): _wifi(Serial3) { //espSerial(ESP_RX, ESP_TX), _wifi(espSerial) {
	init(pFreq, pDataBase);
	Serial.println("WifiManager()!"); //TEST
}

WifiManager::~WifiManager(){
	//delete _wifi;
	//delete _httpConnection;
}


/*********************
* private methods*
*********************/

void WifiManager::init(int pFreq, DataBase* pDataBase) {
	_dataBase = pDataBase;
	initWifi(pFreq);
	initTcpConnection();
	_responseBuilder = new RequestBuilder(_dataBase);
	_responseParser = new ResponseParser(_dataBase);
}

void WifiManager::initWifi(int pFreq) {
	Serial3.begin(pFreq);  //espSerial.begin(9600);
	//_wifi = new ESP8266pro(espSerial);
	// Initialize ESP
	_wifi.begin(eODM_None); // Disable all debug messages
	_findedWANsCount = 0;
	_wifiError = false;
	_SSID = _dataBase->getSSID();
	_ssidPassword = _dataBase->getSsidPassword();
}

void WifiManager::initTcpConnection() {		
	_target = _dataBase->getTarget();//F("/cleverhause/arduino/data");
	_host = _dataBase->getHost();
	_port = _dataBase->getPort();	
	_login = _dataBase->getLogin();
	_password = _dataBase->getPassword();
	_boardUID = String(F("")) + _dataBase->getUniqBaseID();
}

/****************** 
* public interface*
*******************/

bool WifiManager::executeRequest(HttpExchangeType type) {
	if (_wifiError) {
		Serial.println(String(F("Can't send Put Data HTTP request. Wifi initialization fail!")));
		return false;
	}
	//Serial.println("Before StationIP = ");
	delay(1000);
	Serial.println((String)"Free memory >> " + freeMemory());
	
	String request = _responseBuilder->buildRequest(type, _host, _port, _target, _login, _password, _boardUID);
	// Serial.println(String(F("REQUEST:\n")) + request);
	
	ESP8266proClient* con = new ESP8266proClient(_wifi, parseHttpResponse);
	
	if(con->connectTcp(_host, _port)) {		
		con->send(request);
		request = "";
		con->waitResponse();
		_responseParser->parseResponse();
		con->close();
	}	
	delete con;
	
	return true;
}

void WifiManager::fetchFindedWANs(String* _findedWANs) {
	if (_wifi.execute("AT+CWLAP", eCEM_LongTimeOut)) {
		int numLinesCount = _wifi.getLinesCount();
		if (numLinesCount > MAX_FINDED_WAN_COUNT) {
			numLinesCount = MAX_FINDED_WAN_COUNT;
		}
		for (int i = 0; i < numLinesCount; i++) {
			String str = _wifi.getLineItem(i, 1);
			_findedWANs[i] = str;
		}
		_findedWANsCount = numLinesCount;
	}
}

int WifiManager::getFindedWANsCount() {
	return _findedWANsCount;
}

int WifiManager::getMaxFindedWANsCount() {
	return MAX_FINDED_WAN_COUNT;
}

String WifiManager::getFindedWANsDelimiter() {
	return String(FINDED_WAN_DELIMITER);
}

bool WifiManager::connectToWifi() {	
	// if (!espSerial.isListening()) {
		// espSerial.listen();
	// }
	_wifiError = false;
	int repeats = 0;
	String stationIP;
	uint8_t numOfrepeats = 2;
	do
	{
		if (!_wifi.stationConnect(_SSID, _ssidPassword)) {
			delay(1000);
			repeats++;
			Serial.println(F("WifiManager::connectToWifi Connect failed"));
		} else {
			Serial.println(F("WifiManager::connectToWifi Connect is rised!"));
		}
		stationIP = _wifi.stationIP();
	} while (stationIP == NULL_IP && repeats < numOfrepeats);
	
	if (repeats == numOfrepeats) {
		_wifiError = true;
		return false;
	} else {
		Serial.println(String(F("ESP IP: ")) + stationIP);
		return true;
	}
}

void WifiManager::closeConnection() {
	delete _responseBuilder;
	delete _responseParser;
}