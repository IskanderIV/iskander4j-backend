// RFManager
// (c) Ivanov Aleksandr, 2018

#include "RFManager.h"
#include "EepromManager.h"
#include "DataBase.h"

uint8_t rfbuf[RH_NRF24_MAX_MESSAGE_LEN];

RFManager::RFManager(DataBase* pDataBase) {
	init(pDataBase);
	Serial.println("RFManager()!");//TEST
}

RFManager::~RFManager() {
	delete _driver;
	delete _radioMngr;
}

void RFManager::init(DataBase* pDataBase) {
	_dataBase = pDataBase;
	_driver = new RH_NRF24(RADIO_CE_PIN, RADIO_SS_PIN);//RH_Serial(Serial3);
	_radioMngr = new RHReliableDatagram(*_driver, BAZA_ADDRESS);
	_initError = !_radioMngr->init();
	if (_initError) Serial.println(F("RFManager init failed"));
	_radioMngr->setThisAddress(BAZA_ADDRESS);
	_radioMngr->setHeaderFrom(BAZA_ADDRESS);//?????? why is this needed
	_currMsgIndex = 0;
	initDataInfo();
	initRegInfo();
}

void RFManager::initDataInfo() {
	dataInfoUnion.dataInfo._index = _currMsgIndex;
	dataInfoUnion.dataInfo._boardUID = 0L;
	dataInfoUnion.dataInfo._deviceId = 1;
	dataInfoUnion.dataInfo._deviceAck = 0.0;
	dataInfoUnion.dataInfo._deviceControl = 0.0;
	dataInfoUnion.dataInfo._radioError = 0;
}

void RFManager::initRegInfo() {
	regInfoUnion.regInfo._index = _currMsgIndex;
	regInfoUnion.regInfo._boardUID = 0L;
	regInfoUnion.regInfo._deviceId = 1;
	regInfoUnion.regInfo._min = 0.0;
	regInfoUnion.regInfo._max = 0.0;
	regInfoUnion.regInfo._discrete = 0.0;
	regInfoUnion.regInfo._digital = 0;
	regInfoUnion.regInfo._analog = 0;
	regInfoUnion.regInfo._adjustable = 0;
	regInfoUnion.regInfo._rotatable = 0;
	regInfoUnion.regInfo._radioError = 0;
}

/****************
* public methods*
*****************/

bool RFManager::searchDevices(){
	//Serial.println("RFManager::searchDevice() Begin");
	if (_dataBase->getDeviceCount() >= _dataBase->getMaxDevices()) {
		Serial.println(F("max count of devices"));
		//TODO send message to controller to display about this situation
		return false;
	}
	if (_radioMngr->available()) {
        Serial.println(F("BAZA: got something by RF!!"));//TEST
		//input buffer
        uint8_t from;
        uint8_t len = sizeof(rfbuf);
        if (_radioMngr->recvfromAck(regInfoUnion.byteBuffer, &len, &from)) {
			Serial.println(String(F("FROM = ")) + from + "; _boardUID = " + regInfoUnion.regInfo._boardUID);//TEST
			Serial.println(String(F("FROM = ")) + from + "; _deviceId = " + regInfoUnion.regInfo._deviceId);//TEST
			Serial.println(String(F("FROM = ")) + from + "; len = " + len);//TEST
			uint8_t passedIndex = regInfoUnion.regInfo._index;
			uint8_t passedDeviceId = regInfoUnion.regInfo._deviceId;
			uint8_t passedBoardUID = regInfoUnion.regInfo._boardUID;	
			
			if (!isDeviceKnown(passedIndex, passedDeviceId, passedBoardUID)) {
				Serial.println(F("BAZA: No such device found"));//TEST
				uint8_t newDeviceId = _dataBase->generateId();
				Serial.println(String(F("BAZA: new device number = ")) + newDeviceId);//TEST
				prepareDataForKnowingTransmit(newDeviceId);
				// Send a reply back to the originator client
				if (_radioMngr->sendtoWait(regInfoUnion.byteBuffer, sizeof(RegInfo), from)) {
					// Good transmit and Ack. Add this device to eeprom memory and to the dataBase
					registerNewDevice(newDeviceId);
					Serial.println(F("RFManager::searchDevice() Register new Device"));
					return true;
				} else {
					// Bad transmit or Ack. No connection between us. Radio Error of this Device should be true!
					Serial.println(F("BAZA: sendtoWait of unknown failed"));
				}				
			} else {
				if (_radioMngr->sendtoWait(regInfoUnion.byteBuffer, sizeof(RegInfo), from)) {
					// Good transmit and Ack. Add this device to eeprom memory and to the dataBase
					Serial.println(F("RFManager::searchDevice() Good transmit and Ack. Known device. End"));
					return true;					
				} else {
					// Bad transmit or Ack. No connection between us. Radio Error of this Device should be true!
					Serial.println(F("BAZA: sendtoWait 'I know him' failed"));
				}
			}
		}
	}
	//Serial.println("RFManager::searchDevice() No results End");
	return false;
}

void RFManager::processDeveices(){
	Serial.println(F("RFManager::processDeveices() Begin"));
	const int maxDevices = _dataBase->getMaxDevices();
	uint8_t knownDeviceIds[maxDevices];
	memset(knownDeviceIds, 0, maxDevices);
	_dataBase->fetchIds(knownDeviceIds);
	uint8_t deviceId;
	
	// uint8_t len = sizeof(DataInfo);
	// Serial.println(String("len ") + len);//TEST
	for (int i = 0; i < maxDevices; i++) {
		deviceId = knownDeviceIds[i];
		if (deviceId == 0) continue;
		_currMsgIndex += 2;
		
		if(!interaction(deviceId)) {
			Serial.println(String(F("Can't interact with device #")) + String(deviceId));
			_dataBase->setDeviceRFErr(deviceId, true);
		} else {
			_dataBase->setDeviceRFErr(deviceId, false);
		}
	}
	Serial.println(F("RFManager::processDeveices() End"));
}

bool RFManager::hasInitError() {
	return _initError;
}

/*****************
* private methods*
******************/

bool RFManager::interaction(uint8_t To) {
	bool result = false;
	uint8_t len = sizeof(rfbuf);
	prepareDataForWorkingTransmit(To);		
	if(_radioMngr->sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), To)) {
		// Now wait for a reply from Device
		uint8_t from;
		if (_radioMngr->recvfromAckTimeout(dataInfoUnion.byteBuffer, &len, 1000, &from)) {
			Serial.print(String(F("Get data from Device #")) + String(from));
			uint8_t passedIndex = dataInfoUnion.dataInfo._index;
			uint8_t passedDeviceId = dataInfoUnion.dataInfo._deviceId;
			uint8_t passedBoardUID = dataInfoUnion.dataInfo._boardUID;			
			if (isDeviceKnown(passedIndex, passedDeviceId, passedBoardUID)) {
				saveDeviceData(To);
				Serial.print(String(F("Device #")) + String(To) + " data is SAVED \n");
				result = true;
			}
		} else {
			Serial.println(String("Can't receive ack (timeout) from = ") + String(To));
		}
	}
	return result;
}

bool RFManager::isDeviceKnown(uint8_t index, uint8_t deviceId, long boardUID) {
	uint8_t from_id = deviceId;
	bool existedDevice = _dataBase->isDeviceExist(from_id);
	uint8_t passedIndex = index;
	bool isMsgIndexSame = passedIndex == _currMsgIndex;
	Serial.println(String(F("Passed Message index = ")) + String(passedIndex));//TEST
	Serial.println(String(F("Sanded Message index = ")) + String(_currMsgIndex));//TEST
	
	if (existedDevice && boardUID == getUniqID() && isMsgIndexSame) {
		Serial.println(String(F("BAZA: device with id=")) + String(from_id) + F(" is exist & msgIndex is the same"));//TEST
		return true;
	}
	return false;
}

long RFManager::getUniqID() {
	return _dataBase->getUniqBaseID();
	
}

void RFManager::prepareDataForKnowingTransmit(uint8_t pDeviceId) {
	regInfoUnion.regInfo._index = _currMsgIndex;
	regInfoUnion.regInfo._boardUID = getUniqID();
	regInfoUnion.regInfo._deviceId = pDeviceId;
}

void RFManager::prepareDataForWorkingTransmit(uint8_t pDeviceId) {
	dataInfoUnion.dataInfo._index = _currMsgIndex;
	dataInfoUnion.dataInfo._boardUID = getUniqID();
	dataInfoUnion.dataInfo._deviceId = pDeviceId;
	dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue(pDeviceId);
	
	// dataInfoUnion.dataInfo._deviceControl = blink[pDeviceId] ? 1.0 : 0.0; //STUB
	Serial.println(String(F("Control value = ")) + dataInfoUnion.dataInfo._deviceControl);	
	// blink[pDeviceId] = !blink[pDeviceId]; //STUB
	
}

void RFManager::saveDeviceData(uint8_t pDeviceId) {
	_dataBase->setDeviceAck(pDeviceId, dataInfoUnion.dataInfo._deviceAck);
	_dataBase->setDeviceRFErr(pDeviceId, itob(dataInfoUnion.dataInfo._radioError));
	Serial.println(String(F("passedDeviceAck = ")) + String(dataInfoUnion.dataInfo._deviceAck));
}

void RFManager::registerNewDevice(uint8_t pDeviceId) {
	_dataBase->addDeviceInfo(pDeviceId);
	_dataBase->setDeviceMin(pDeviceId, regInfoUnion.regInfo._min);
	_dataBase->setDeviceMax(pDeviceId, regInfoUnion.regInfo._max);
	_dataBase->setDeviceDiscrete(pDeviceId, regInfoUnion.regInfo._discrete);
	_dataBase->setDeviceDigital(pDeviceId, itob(regInfoUnion.regInfo._digital));
	_dataBase->setDeviceAnalog(pDeviceId, itob(regInfoUnion.regInfo._analog));
	_dataBase->setDeviceAdj(pDeviceId, itob(regInfoUnion.regInfo._adjustable));
	_dataBase->setDeviceRotatable(pDeviceId, itob(regInfoUnion.regInfo._rotatable));
	_dataBase->setDeviceRFErr(pDeviceId, itob(regInfoUnion.regInfo._radioError));
	_dataBase->saveDevicesIdsToEeprom();
}
