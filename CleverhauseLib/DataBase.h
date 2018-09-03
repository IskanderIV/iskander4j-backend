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
	
	bool isDeviceExist(uint8_t _id);
	uint8_t generateId();
	void addDeviceInfo(uint8_t _id);
	void removeDeviceInfo(char _id);
	
	// device info
	float getDeviceAck(char _id);
	void setDeviceAck(char _id, float _ack);
	float getDeviceMin(uint8_t _id);
	void setDeviceMin(uint8_t _id, float _min); //memorized
	float getDeviceMax(uint8_t _id);
	void setDeviceMax(uint8_t _id, float _max); //memorized
	float getDeviceDiscrete(uint8_t _id);
	void setDeviceDiscrete(uint8_t _id, float _discrete); //memorized
	float getDeviceControlValue(char _id);
	void setDeviceControlValue(char _id, float _control); //memorized from response
	bool getDeviceDigital(uint8_t _id);
	void setDeviceDigital(uint8_t _id, bool _digital);//memorized
	bool getDeviceAnalog(uint8_t _id);
	void setDeviceAnalog(uint8_t _id, bool _analog);//memorized
	bool getDeviceAdj(uint8_t _id);
	void setDeviceAdj(uint8_t _id, bool _adj); //memorized
	bool getDeviceRotatable(uint8_t _id);
	void setDeviceRotatable(uint8_t _id, bool _rotatable); //memorized
	bool getDeviceRFErr(char _id);
	void setDeviceRFErr(char _id, bool _radioError);
	
	void fetchIds(uint8_t* _idsBuffer);
	
	void saveDevicesIdsToEeprom();
	
	// WIFI
	void setSSID(String _SSID);
	String getSSID();
	void setSsidPassword(String _ssidPassword);
	String getSsidPassword();
	
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
	
	// MAX LENGTHS
	int getMaxLenOfSsid();
	int getMaxLenOfSsidPassword();
	int getMaxLenOfLogin();
	int getMaxLenOfPassword();
	int getMaxLenOfHost();
	int getMaxLenOfPort();
	int getMaxLenOfTarget();
	int getMaxDevices();
	int getMaxLenOfBoardUidSymbols();
	
	int  getDeviceCount();
	
	long getUniqBaseID();
	void setUniqBaseID(long pUniqBaseID);
	
	// global errors
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
		float getMin();
		void  setMin(float _min);
		float getMax();
		void  setMax(float _max);
		float getDiscrete();
		void  setDiscrete(float _discrete);
		float getControlVal();
		void  setControlVal(float _controlValue);
		bool  getDigital();
		void  setDigital(bool _digital);
		bool  getAnalog();
		void  setAnalog(bool _analog);
		bool  getAdjustable();
		void  setAdjustable(bool _adjustable);
		bool  getRotatable();
		void  setRotatable(bool _rotatable);
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
		float _min;
		float _max;
		float _discrete;
		float _controlValue;
		bool  _digital;
		bool  _analog;
		bool _adjustable;		
		bool  _rotatable;
		bool _radioError;
	};
	
private:
	class DeviceInfo;

	//methods
	void init();
	void initFromEeprom();
	void fillDeviceInfoFromEeprom(uint8_t id);
	DeviceInfo* findDeviceInfo(char _id);
	void addDeviceFirst(DeviceInfo* added);
	void addDeviceLast(DeviceInfo* added);
	
	//fields
	EepromManager* _eepromMngr;	
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
	
	int  _maxLenOfSsid;
	int  _maxLenOfSsidPassword;
	int  _maxLenOfLogin;
	int  _maxLenOfPassword;
	int  _maxLenOfHost;
	int  _maxLenOfPort;
	int  _maxLenOfTarget;
	uint8_t _maxDevices;
	int  _maxLenOfBoardUidSymbols;
};

#endif