// ResponseParser
// (c) Ivanov Aleksandr, 2018

#ifndef _ResponseParser_H_
#define _ResponseParser_H_

#include "DataBase.h"

class DataBase;

class ResponseParser : public Object
{
public:
	ResponseParser();
	~ResponseParser();
	
	// public interfaces
	bool parseDataRequest();
	bool parseStructureRequest();
	
private:
	DataBase* _dataBase;
	
	void init();
	bool parseHeaders();
	void parseHeader();
	void parseBody();
	int findJsonElementByKey(String key, int begPos);
};

#endif