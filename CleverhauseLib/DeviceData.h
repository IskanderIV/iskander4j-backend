// DeviceData.h
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceData_H_
#define _DeviceData_H_

//#define DEBUG

class DeviceData
{
public:
	DeviceData(String& _payload);
	~DeviceData();
	
	String getPayload();
	void setPayload(String& _payload);
	
	
private:
	uint8_t _id;
};

#endif