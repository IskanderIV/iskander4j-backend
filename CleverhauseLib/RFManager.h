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
class DataBase;

struct DataInfo {
	long  _uniqID;
	uint8_t  _deviceID;
	float _deviceAck; //INPUT
	float _min; //INPUT
	float _max; //INPUT
	float _discrete; //INPUT
	float _deviceControl;
	uint8_t  _digital;//INPUT
	uint8_t  _analog;//INPUT
	uint8_t  _adjustable;//INPUT
	uint8_t  _rotatable;
	uint8_t  _radioError; //INPUT
};

extern uint8_t buf[RH_ASK_MAX_MESSAGE_LEN]; // TODO try to experiment with that

class RFManager : public Object
{
public:
	RFManager(DataBase* pDataBase);
	~RFManager();
	
	// interface impl for controllers events
	bool searchDevices();
	void processDeveices();
	bool hasInitError();
	
private:
	union DataInfoUnion {  
		DataInfo dataInfo;  
		uint8_t byteBuffer[sizeof(DataInfo)];  
	} dataInfoUnion;
	
	RH_ASK* _driver;
	RHReliableDatagram* _radioMngr;
	DataBase* _dataBase;
	bool _initError;
	bool* blink; //TEST
	
	//methods
	void init(DataBase* pDataBase);
	long getUniqID();
	bool isDeviceKnown(uint8_t _from);
	void prepareDataForKnowingTransmit(uint8_t pDeviceId);
	void prepareDataForWorkingTransmit(uint8_t pDeviceId);
	void saveDeviceData(uint8_t pDeviceId);	
	void addDeviceToDataBase(uint8_t pDeviceId);
	void registerNewDevice(uint8_t pDeviceId);
	
	bool itob(int in) {
		return in == 0 ? false : true;
	};
	
	uint8_t btoi(bool in) {
		return in == true ? 1 : 0;
	};
};

#endif