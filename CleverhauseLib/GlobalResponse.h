// GlobalResponse.h
// (c) Ivanov Aleksandr, 2018

#ifndef _GlobalResponse_H_
#define _GlobalResponse_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

extern String response;
extern int numOfHeaders;
extern String headers;
extern int headerIndex;
extern String body;
extern boolean currentLineIsBlank;
extern boolean isInsideBody;
// extern void parseHttpResponse(ESP8266proConnection* connection,
                   // char* buffer, int length, boolean completed);

#endif