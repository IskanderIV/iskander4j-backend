// GlobalResponse.cpp
// (c) Ivanov Aleksandr, 2018

#include "GlobalResponse.h"

String response = "";
int numOfHeaders = 6; 
String headers = "";
int headerIndex = 0;
String body = "";
boolean currentLineIsBlank = true;
boolean isInsideBody = false;

/*****************************
Here parsing of RESPONSE
******************************/

void WifiManager::parseHttpResponse(ESP8266proConnection* connection,
                   char* buffer, int length, boolean completed) {
	/*
	Serial.println(F("RESPONSE"));
	*/
	response += buffer;
}