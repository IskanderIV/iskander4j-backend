// RFManager
// (c) Ivanov Aleksandr, 2018

#ifndef _RFManager_H_
#define _RFManager_H_

#include "Object.h"
#include <RH_NRF24.h>
#include <RHReliableDatagram.h>
#include <SPI.h>

#define BAZA_ADDRESS 0

#define RADIO_CE_PIN 48
#define RADIO_SS_PIN 53

#define RADIO_FREG 4000

//#define DEBUG
class RH_NRF24;
class RHReliableDatagram;
class DataBase;

struct DataInfo {
	uint8_t _index;
	long  _boardUID;
	uint8_t  _deviceId;
	float _deviceAck; //INPUT
	float _deviceControl;
	uint8_t  _radioError; //INPUT
};

struct RegInfo {
	uint8_t _index;
	long  _boardUID;
	uint8_t  _deviceId;
	float _min; //INPUT
	float _max; //INPUT
	float _discrete; //INPUT
	uint8_t  _digital;//INPUT
	uint8_t  _analog;//INPUT
	uint8_t  _adjustable;//INPUT
	uint8_t  _rotatable;//INPUT
	uint8_t  _radioError; //INPUT
};

extern uint8_t rfbuf[RH_NRF24_MAX_MESSAGE_LEN]; // TODO try to experiment with that

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
	union RegInfoUnion {  
		RegInfo regInfo;  
		uint8_t byteBuffer[sizeof(RegInfo)];  
	} regInfoUnion;
	
	RH_NRF24* _driver;
	RHReliableDatagram* _radioMngr;
	DataBase* _dataBase;
	bool _initError;
	uint8_t _currMsgIndex;
	
	//methods
	void init(DataBase* pDataBase);
	void initDataInfo();
	void initRegInfo();
	bool interaction(uint8_t _To);
	long getUniqID();
	bool isDeviceKnown(uint8_t index, uint8_t deviceId, long boardUID);
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