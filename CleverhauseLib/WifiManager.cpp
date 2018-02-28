// WifiManager
// (c) Ivanov Aleksandr, 2018

#include "WifiManager.h"
#include "EepromManager.h"
#include "DataBase.h"

WifiManager::WifiManager(int pFreq): _wifi(Serial3) { //espSerial(ESP_RX, ESP_TX), _wifi(espSerial) {
	initWifi(pFreq);
	initTcpConnection();
	Serial.println("WifiManager()!");//TEST
}

WifiManager::~WifiManager(){
	//delete _wifi;
	//delete _httpConnection;
}

/* 
* interface impl for controllers requests 
*
*/

bool WifiManager::connectToWifi() {	
	// if (!espSerial.isListening()) {
		// espSerial.listen();
	// }
	_wifiError = false;
	int repeats = 0;
	String ssid = _eepromMngr->fetch(eepr_wifiLogin);
	String password = _eepromMngr->fetch(eepr_wifiPsswd);
	// String ssid = "acer Liquid Z630";
	// String password = "111222333";
	String stationIP;
	uint8_t numOfrepeats = 2;//3
	do
	{
		if (!_wifi.stationConnect(ssid, password)) {
			delay(1000);
			repeats++;
			Serial.println("WifiManager::connectToWifi Connect failed");
		} else {
			Serial.println("WifiManager::connectToWifi Connect is rised!");
		}
		stationIP = _wifi.stationIP();
	} while (stationIP == NULL_IP && repeats < numOfrepeats);
	
	if (repeats == numOfrepeats) {
		_wifiError = true;
		return false;
	} else {
		Serial.print("ESP IP: ");
		Serial.println(stationIP);
		return true;
	}
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

void WifiManager::closeConnection() {
	
}

bool WifiManager::executePutRequest() {
	_noTcpConnection = false;
	_cantSend = false;
	_noRessponse = false;
	_cantClose = false;
	//Serial.println("Before StationIP = ");
	delay(1000);
	Serial.println(String("_serverAdress = ") + _serverAdress);
	Serial.println(String("_serverPort = ") + _serverPort);
	String request = buildRequest();
	Serial.println(String("REQUEST:\n") + request);
	ESP8266proClient* con = new ESP8266proClient(_wifi, parseHttpResponse);
	//Serial.println(String("_wifi.stationIP = ") + _wifi.stationIP());
	/*
	if(!con.connectTcp(_serverAdress, _serverPort)) {
		Serial.println(String("Con.Id = ") + con.getId());
		if (con.getId() != ESP_INVALID_CONNECTION) {
			con.close();
		}
		_wifi.execute("", eCEM_NoLineBreak);
		Serial.println((_eepromMngr->fetch(eepr_serverPort)).toInt());
		Serial.println(F("Can't connect to server!"));
		_noTcpConnection = true;
		return false;
	}
	//Serial.println(F("Good connect to server!"));
	*/
	// if (_wifi.getConnectionId(con) == ESP_INVALID_CONNECTION) {
		// _wifi.removeConnection(con);
	// }
	// Serial.println("AT+CIPSTATUS");
	// _wifi.execute("AT+CIPSTATUS");
	if(con->connectTcp(_serverAdress, _serverPort)) {
		con->send(request);
		con->waitResponse();
		con->close();
	}
	
	delete con;
	/*
	//Serial.println(request);
	//Serial.println(F("Request is build!"));
	if (!con.send(request)) {
	// if (!_httpConnection->send(request)) {
		_cantSend = true;
		Serial.println(F("Can't send!"));
		return false;		
	}
	Serial.println(F("Good send!"));
	Serial.println(F("Wait response>>>>>"));
	if (!con.waitResponse()) {
	// if (!_httpConnection->waitResponse()) {
		_noRessponse = true;
		Serial.println(F("No response!"));
		return false;
	}
	Serial.println(F("Try to close connection>>>>>"));
	if (!con.close()) {
	// if (!_httpConnection->close()) {
		_cantClose = true;
		Serial.println(F("Can't close!"));
		return false;
	}
	Serial.println(F("executePutRequest end!"));
	*/
	return true;
}

void WifiManager::setDataBase(DataBase* pDataBase) {
	_dataBase = pDataBase;
}

void WifiManager::setEepromManager(EepromManager* pEepromMngr) {
	_eepromMngr = pEepromMngr;	
	_serverAdress = _eepromMngr->fetch(eepr_serverAdress);
	_serverPort = (_eepromMngr->fetch(eepr_serverPort)).toInt();
	Serial.println((_eepromMngr->fetch(eepr_serverPort)).toInt());
}

/* 
* private methods
*
*/

void WifiManager::initWifi(int pFreq) {
	Serial3.begin(9600);  //espSerial.begin(9600);
	//_wifi = new ESP8266pro(espSerial);
	// Initialize ESP
	_wifi.begin(eODM_None); // Disable all debug messages
	_findedWANsCount = 0;
	_wifiError = false;
}

void WifiManager::initTcpConnection() {
	//_httpConnection = new ESP8266proClient(_wifi, parseHttpResponse);
	
	// _noConnection = false;
	// _cantSend = false;
	// _noRessponse = false;
	// _cantClose = false;
}

void parseHttpResponse(ESP8266proConnection* connection,
                   char* buffer, int length, boolean completed) {
	//Serial.println(F("Buffer>>>>>"));
	//Serial.println(strlen(buffer));
	//Serial.println(buffer);
}

String WifiManager::buildRequest() {
	// String requestBody = F("{\"devicesState\": [],\"controllerErrors\": {\"gsmError\": \"false\",\"lcdError\" : \"false\",\"radioError\" : \"false\"}}");
	String requestBody = formRequestJson();
	String request = String(""); 
	request += F("PUT /arduino/data HTTP/1.1"); // TODO "/arduino/data" - need to be in memory
	request += END_OF_STRING;
	//request += F("Host: localhost:8090");
	request += F("Host: ");
	request += _serverAdress + ":";
	request += _serverPort;
	request += END_OF_STRING;	
	request += F("Content-Length: ");
	request += requestBody.length();
	request += END_OF_STRING;
	request += F("Content-Type: application/json");
	request += END_OF_STRING;
	request += END_OF_STRING;
	request += requestBody;
	
	return request;	
}

String WifiManager::formRequestJson() {
	String requestJson = String("{");
	//DEVICE STATE BLOK
	requestJson += String("") + "\"" + String(DEVICES_STATES_BLOCK_NAME) + "\"" + ":";
	requestJson += "[";		
	int deviceCount = _dataBase->getDeviceCount();
	if (deviceCount) {
		uint8_t* eepr_deviceIds = new uint8_t[deviceCount];
		_dataBase->fetchIds(eepr_deviceIds);
		for(int i = 0; i < deviceCount - 1; i++) {
			requestJson += buildRequestDeviceJson((char)eepr_deviceIds[i]);
			requestJson += ",";
		}
		requestJson += buildRequestDeviceJson(eepr_deviceIds[deviceCount - 1]);
		delete[] eepr_deviceIds;
	}	
	requestJson += "]";
	requestJson += ",";
	// GLOBAL ERRORS BLOCK
	requestJson += String("") + "\"" + String(GLOBAL_ERRORS_BLOCK_NAME) + "\"" + ":";
	requestJson += buildRequestGlobalErrorsJson();
	requestJson += "}";
	
	return requestJson;
}

String WifiManager::buildRequestDeviceJson(char id) {
	float ack = _dataBase->getDeviceAck(id);
	bool adjustable = _dataBase->getDeviceAdj(id);
	float controlValue = _dataBase->getDeviceControlValue(id);
	bool radioError = _dataBase->getDeviceRFErr(id);
	String deviceJsonString = String("{");
	deviceJsonString += String("") + "\"" + String(DEVICE_ID_KEY_NAME) + "\"" + ":";
	deviceJsonString += (uint8_t) id;
	deviceJsonString += ",";
	deviceJsonString += String("") + "\"" + String(DEVICE_ACK_KEY_NAME) + "\"" + ":";
	deviceJsonString += ack; //TODO format
	deviceJsonString += ",";
	deviceJsonString += String("") + "\"" + String(DEVICE_ADJUSTABLE_KEY_NAME) + "\"" + ":";
	if (adjustable) {
		deviceJsonString += "true";
	} else {
		deviceJsonString += "false";
	}
	deviceJsonString += ",";
	deviceJsonString += String("") + "\"" + String(DEVICE_CONTROL_VALUE_KEY_NAME) + "\"" + ":";
	deviceJsonString += controlValue; // TODO format
	deviceJsonString += ",";
	deviceJsonString += String("") + "\"" + String(DEVICE_RADIO_ERROR_KEY_NAME) + "\"" + ":";
	if (radioError) {
		deviceJsonString += "true";
	} else {
		deviceJsonString += "false";
	}
	deviceJsonString += "}";
	return deviceJsonString;
}

String WifiManager::buildRequestGlobalErrorsJson() {
	bool gsmError = _dataBase->getGsmError();
	bool radioError = _dataBase->getRadioError();
	bool lcdError = _dataBase->getLcdError();
	String globalErrorsJsonString = String("{");
	globalErrorsJsonString += String("") + "\"" + String(GSM_ERROR_KEY_NAME) + "\"" + ":";
	globalErrorsJsonString += gsmError ? "true" : "false";
	globalErrorsJsonString += ",";
	globalErrorsJsonString += String("") + "\"" + String(RADIO_ERROR_KEY_NAME) + "\"" + ":";
	globalErrorsJsonString += radioError ? "true" : "false";
	globalErrorsJsonString += ",";
	globalErrorsJsonString += String("") + "\"" + String(LCD_ERROR_KEY_NAME) + "\"" + ":";
	globalErrorsJsonString += lcdError ? "true" : "false";	
	globalErrorsJsonString += "}";
	return globalErrorsJsonString;
}