// DataBase
// (c) Ivanov Aleksandr, 2018

#ifndef _DataBase_H_
#define _DataBase_H_

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class EepromManager;

class DataBase
{
public:
	DataBase();
	~DataBase();
	
	bool  isDeviceExist(char _id);
	void  addDeviceInfo(char _id);
	void  removeDeviceInfo(char _id);
	float getDeviceAck(char _id);
	void  setDeviceAck(char _id, float _ack);
	bool  getDeviceAdj(char _id);
	void  setDeviceAdj(char _id, bool _adj);
	float getDeviceControlValue(char _id);
	void  setDeviceControlValue(char _id, float _control);
	bool  getDeviceRFErr(char _id);
	void  setDeviceRFErr(char _id, bool _radioError);
	void  fetchIds(uint8_t* _idsBuffer);
	void  saveDevicesIdsToEeprom();
	
	// WIFI
	void  setSSID(String _SSID);
	String  getSSID();
	void  setSsidPassword(String _ssidPassword);
	String  getSsidPassword();
	
	// TCP
	void  setLogin(String _login);
	String  getLogin();
	void  setPassword(String _password);
	String  getPassword();
	
	// Site
	void  setHost(String _host);
	String  getHost();
	void  setPort(String _port);
	String  getPort();
	void  setTarget(String _target);
	String  getTarget();
	
	int  getDeviceCount();
	
	long getUniqBaseID();
	void setUniqBaseID(long pUniqBaseID);
	bool getGsmError();
	void setGsmError(bool pGsmError);
	bool getRadioError();
	void setRadioError(bool pRadioError);
	bool getLcdError();
	void setLcdError(bool pLcdError);
	
	void setEepromManager(EepromManager* _eepromMngr);
		
	class DeviceInfo
	{
	public:
		DeviceInfo(char _id);
		~DeviceInfo();
		
		char  getId();
		void  setId(char _id);
		float getAck();
		void  setAck(float _ack);
		bool  getAdjustable();
		void  setAdjustable(bool _adjustable);
		float getControlVal();
		void  setControlVal(float _controlValue);
		bool  hasRadioError();
		void  setRadioError(bool _radioError);
		
		DeviceInfo* getPrev();
		void setPrev(DeviceInfo* _DeviceInfo);
		DeviceInfo* getNext();
		void setNext(DeviceInfo* _DeviceInfo);
		
	private:
		DeviceInfo* _prev;
		DeviceInfo* _next;
		char _deviceId;
		float _deviceAck;
		bool _adjustable;
		float _controlValue;
		bool _radioError;
	};
	
private:
	class DeviceInfo;

	//methods	
	void initFromEeprom();
	DeviceInfo* findDeviceInfo(char _id);
	void addDeviceFirst(DeviceInfo* added);
	void addDeviceLast(DeviceInfo* added);
	
	//fields
	EepromManager* _eepromMngr;
	uint8_t _maxDevices;
	DeviceInfo* _deviceJsonList;
	DeviceInfo* _lastDeviceJson;
	int _deviceCount;
	long _uniqBaseID;
	
	String _SSID;
	String _ssidPassword;
	
	String _login;
	String _password;
	
	String _host;
	String _port;
	String _target;
	
	bool _gsmError;
	bool _radioError;
	bool _lcdError;
};

#endif