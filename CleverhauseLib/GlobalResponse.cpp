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

