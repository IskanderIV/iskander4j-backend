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
	// _target = _dataBase->getTarget();//F("/cleverhause/arduino/data");
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
	bool result = true;
	//Serial.println("Before StationIP = ");
	delay(1000);
	Serial.println((String)"Free memory >> " + freeMemory());
	
	ESP8266proClient* con = new ESP8266proClient(_wifi, WifiManager::saveHttpResponse);
	
	if(con->connectTcp(_host, _port.toInt())) {		
		prepareAndSendRequest(type, con);
		parseHttpResponse(type, con);
		
		// int maxRepetitions = 2;
		// int repetition = 0;
		// while(!con->close() && ++repetition < maxRepetitions){
			// con->waitResponse(2000);
			// Serial.println(String(F("WAIT CLOSE CONNECTION...."))); //TEST
		// }
		
		if (!con->close()) {
			result = false;
		}
		Serial.println(String(F("WHEN CLOSE CONNECTION...."))); //TEST
	} else {
		result = false;
	}
	delete con;
	// Serial.println((String)F("Free memory before out executeRequest >> ") + freeMemory());//TEST
	
	return result;
}

void WifiManager::prepareAndSendRequest(HttpExchangeType type, ESP8266proClient* con) {
	_requestBuilder = new RequestBuilder(_dataBase);
	String request = _requestBuilder->buildRequest(type, _host, _port, _login, _password, _boardUID);
	delete _requestBuilder;
		Serial.println((String)F("Free memory after build request>> ") + freeMemory());//TEST
	con->send(request);
		Serial.println((String)F("Free memory after send(request)>> ") + freeMemory());//TEST
	request = "";
		Serial.println((String)F("Free memory before exit prepareAndSendRequest>> ") + freeMemory());//TEST
}

void WifiManager::parseHttpResponse(HttpExchangeType type, ESP8266proClient* con) {
	con->waitResponse();
	Serial.println((String)F("Free memory before parseResponse>> ") + freeMemory());//TEST
	
	_responseParser = new ResponseParser(_dataBase);
	_responseParser->parseResponse(type);
	delete _responseParser;
	
	Serial.println((String)F("Free memory before exit parseResponse>> ") + freeMemory());//TEST
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
			delay(500);
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

void WifiManager::disconnectFromWifi() {
	if (!_wifi.stationDisconnect()) {
		delay(500);
		Serial.println(F("Disconnect From Wifi failed"));
	} else {
		Serial.println(F("Disconnect From Wifi is done!"));
	}
}

/*****************************
Here parsing of RESPONSE
******************************/

void WifiManager::saveHttpResponse(ESP8266proConnection* connection, char* buffer, int length, boolean completed) {
	/*
	Serial.println(F("RESPONSE"));
	*/
	response += buffer;
	
}