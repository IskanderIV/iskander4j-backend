// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceRFManager_H_
#define _DeviceRFManager_H_

#include <RH_ASK.h>
#include <RHReliableDatagram.h>
#include <SPI.h>

#define BAZA_ADDRESS 0

#define RADIO_TX_PIN 12
#define RADIO_RX_PIN 11
//MINI2
//#define RADIO_FREG 4000
#define RADIO_FREG 2000

//#define DEBUG

class RH_ASK;
class RHReliableDatagram;
class DeviceDataBase;

struct DataInfo {
	long  _boardUID; //INPUT in STRUCT/OUTPUT
	uint8_t  _deviceId; //INPUT in STRUCT/OUTPUT
	float _deviceAck; //OUTPUT
	float _min; //OUTPUT
	float _max; //OUTPUT
	float _discrete; //OUTPUT
	float _deviceControl; //INPUT in DATA
	uint8_t  _digital;//OUTPUT
	uint8_t  _analog;//OUTPUT
	uint8_t  _adjustable; //OUTPUT
	uint8_t  _rotatable; //OUTPUT
	uint8_t  _radioError; //OUTPUT
};

extern uint8_t rfBuf[RH_ASK_MAX_MESSAGE_LEN];

class DeviceRFManager
{
private:
	union DataInfoUnion {  
		DataInfo dataInfo;  
		uint8_t byteBuffer[sizeof(DataInfo)];  
	} dataInfoUnion;
	RH_ASK _driver;
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
	long getBoardUID();
	void updateStructureData();
	bool isDataMessageForMe(uint8_t from);
	void updateControlFromBoard(float newControlData);	
	void fixWrongRFConnection();
	void saveBoardData();
	void prepareDataForTransmit();
	
	bool itob(int in) {
		return in == 0 ? false : true;
	};
	
	uint8_t btoi(bool in) {
		return in == true ? 1 : 0;
	};
};

#endif