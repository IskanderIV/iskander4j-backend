// RFManager
// (c) Ivanov Aleksandr, 2018

#ifndef _RFManager_H_
#define _RFManager_H_

#include "Object.h"
#include <RH_ASK.h>
#include <RHReliableDatagram.h>
#include <SPI.h>

#define BAZA_ADDRESS 0

#define RADIO_TX_PIN 12
#define RADIO_RX_PIN 11

#define RADIO_FREG 2000

//#define DEBUG
class RH_ASK;
class RHReliableDatagram;
class EepromManager;
class DataBase;

struct DataInfo {
	long  _uniqID;
	uint8_t  _deviceID;
	float _deviceAck;
	float _deviceControl;
	bool  _adjustable;
	bool  _rotatable;
	bool  _radioError; //INPUT
};

extern uint8_t buf[RH_ASK_MAX_MESSAGE_LEN];

class RFManager : public Object
{
public:
	RFManager();
	~RFManager();
	
	// interface impl for controllers events
	bool searchDevices();
	void processDeveices();
	void setDataBase(DataBase* _dataBase);
	void setEepromManager(EepromManager* _eepromMngr);
	
private:
	union DataInfoUnion {  
		DataInfo dataInfo;  
		uint8_t byteBuffer[sizeof(DataInfo)];  
	} dataInfoUnion;
	RH_ASK* _driver;
	RHReliableDatagram* _radioMngr;
	EepromManager* _eepromMngr;
	DataBase* _dataBase;
	bool _initError;
	bool* blink; //TEST
	
	//methods
	void init();
	long getUniqID();
	bool isDeviceKnown(uint8_t _from, uint8_t* _knownDeviceIds, uint8_t _knownDeviceIdsCount);
	void prepareDataForKnowingTransmit(uint8_t _deviceNumber);
	void prepareDataForWorkingTransmit(uint8_t _deviceNumber);
	void saveDeviceData(uint8_t _deviceNumber);	
	void addDeviceToDataBase(uint8_t _deviceNumber);
};

#endif