// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceRFManager_H_
#define _DeviceRFManager_H_

#include <RH_NRF24.h>
#include <RHReliableDatagram.h>
#include <SPI.h>

#define BAZA_ADDRESS 0

#define RADIO_CE_PIN 8
#define RADIO_SS_PIN 10
//MINI2
//#define RADIO_FREG 4000
#define RADIO_FREG 4000

//#define DEBUG

class RH_NRF24;
class RHReliableDatagram;
class DeviceDataBase;

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
	long  _boardUID;//INPUT
	uint8_t  _deviceId;//INPUT
	float _min; 
	float _max; 
	float _discrete; 
	uint8_t  _digital;
	uint8_t  _analog;
	uint8_t  _adjustable;
	uint8_t  _rotatable;
	uint8_t  _radioError;
};

extern uint8_t rfBuf[RH_NRF24_MAX_MESSAGE_LEN];

class DeviceRFManager
{
private:
	union DataInfoUnion {  
		DataInfo dataInfo;  
		uint8_t byteBuffer[sizeof(DataInfo)];  
	} dataInfoUnion;
	union RegInfoUnion {
		RegInfo regInfo;
		uint8_t byteBuffer[sizeof(RegInfo)];  
	} regInfoUnion;
	
	RH_NRF24 _driver;
	RHReliableDatagram _radioMngr;
	DeviceDataBase* _dataBase;
	bool _initError;
	
public:
	DeviceRFManager(DeviceDataBase* _dataBase);
	~DeviceRFManager();
	
	// interface impl for controllers events
	bool identifyDevice();
	void sendInfo();

private:
	//methods
	void init();
	void initDataInfo();
	void initRegInfo();
	bool interaction(uint8_t _To);
	long getBoardUID();
	void updateStructureData();
	bool isDataMessageForMe(uint8_t from, uint8_t deviceId, long boardUID);
	void updateControlFromBoard(float newControlData);	
	void fixWrongRFConnection();
	void saveBoardData();
	void prepareDataForRegTransmit();
	void prepareDataForDataTransmit();
	
	bool itob(int in) {
		return in == 0 ? false : true;
	};
	
	uint8_t btoi(bool in) {
		return in == true ? 1 : 0;
	};
};

#endif