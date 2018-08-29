// GlobalResponse.h
// (c) Ivanov Aleksandr, 2018

#ifndef _GlobalResponse_H_
#define _GlobalResponse_H_

extern String response;
extern int numOfHeaders;
extern String headers;
extern int headerIndex;
extern String body;
extern boolean currentLineIsBlank;
extern boolean isInsideBody;
extern void parseHttpResponse(ESP8266proConnection* connection,
                   char* buffer, int length, boolean completed);

#endif