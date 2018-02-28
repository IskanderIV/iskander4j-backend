// RFManager
// (c) Ivanov Aleksandr, 2018

#include "RFManager.h"
#include "EepromManager.h"
#include "DataBase.h"

RFManager::RFManager() {
	blink = new bool[MAX_DEVICES];
	for (uint8_t i = 0; i < MAX_DEVICES; i++) {
		if (i%2) {
			blink[i] = false;
		} else {
			blink[i] = true;
		}		
	}
	init();
	Serial.println("RFManager()!");//TEST
}

RFManager::~RFManager() {
	delete _driver;
	delete _radioMngr;
}

/* 
* interface impl for controllers requests 
*
*/

bool RFManager::searchDevices(){
	//Serial.println("RFManager::searchDevice() Begin");
	if (_dataBase->getDeviceCount() >= MAX_DEVICES) {
		//TODO send message to controller to display about this situation
		return false;
	}
	if (_radioMngr->available()) {
        Serial.println("BAZA: got something by RF!!");//TEST
		//input buffer
        uint8_t from;
        uint8_t len = sizeof(DataInfo);
        if (_radioMngr->recvfromAck(dataInfoUnion.byteBuffer, &len, &from)) {
			Serial.println(String("FROM = ") + from + "; _uniqID = " + dataInfoUnion.dataInfo._uniqID);
			Serial.println(String("FROM = ") + from + "; _deviceID = " + dataInfoUnion.dataInfo._deviceID);
			Serial.println(String("FROM = ") + from + "; len = " + len);
			uint8_t knownDeviceIdsCount = _dataBase->getDeviceCount();
			uint8_t knownDeviceIds[knownDeviceIdsCount];
			//Serial.println(String("FROM = ") + from);//TEST
			bool knownDevice = isDeviceKnown(from, knownDeviceIds, knownDeviceIdsCount);
			Serial.println(String("knownDevice = ") + knownDevice);
			if (!knownDevice) {
				Serial.println("BAZA: No such device found");//TEST
				uint8_t device_number = knownDeviceIdsCount + 1;
				Serial.print("BAZA: new device number = ");//TEST
				Serial.println(device_number);//TEST
				prepareDataForKnowingTransmit(device_number);
				// Send a reply back to the originator client
				//serialPrint("BAZA: data = ", data[0]);//TEST
				//serialPrint("BAZA: from = ", from);//TEST
				if (_radioMngr->sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), from)) {
					// Good transmit and Ack. Add this device to eeprom memory and to the dataBase
					addDeviceToDataBase(device_number);
					Serial.println("RFManager::searchDevice() Good transmit and Ack I don't know you End");
					return true;					
				} else {
					// Bad transmit or Ack. No connection between us. Radio Error of this Device should be true!
					Serial.println("BAZA: sendtoWait unknown failed");
				}				
			} else {
				if (_radioMngr->sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), from)) {
					// Good transmit and Ack. Add this device to eeprom memory and to the dataBase
					Serial.println("RFManager::searchDevice() Good transmit and Ack I know you End");
					return true;					
				} else {
					// Bad transmit or Ack. No connection between us. Radio Error of this Device should be true!
					Serial.println("BAZA: sendtoWait I know him failed");
				}
			}
		}
	}
	//Serial.println("RFManager::searchDevice() No results End");
	return false;
}

void RFManager::processDeveices(){
	Serial.println("RFManager::processDeveices() Begin");
	uint8_t knownDeviceIds[MAX_DEVICES];
	memset(knownDeviceIds, 0, MAX_DEVICES);
	_dataBase->fetchIds(knownDeviceIds);
	uint8_t device_number;
	uint8_t len = sizeof(buf);
	// uint8_t len = sizeof(DataInfo);
	Serial.println(String("len ") + len);//TEST
	for (int i = 0; i < MAX_DEVICES; i++) {
		device_number = knownDeviceIds[i];		
		if (device_number == 0) continue;
		Serial.println(String("device_number ") + device_number);//TEST
		prepareDataForWorkingTransmit((char) device_number);
		uint8_t from;
		if (_radioMngr->sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), device_number)) {
			// Now wait for a reply from Device			
			if (_radioMngr->recvfromAckTimeout(dataInfoUnion.byteBuffer, &len, 2000, &from)) {
				Serial.print("Get data from Device 0x"); Serial.println(from);
				if (from == device_number && dataInfoUnion.dataInfo._uniqID == getUniqID()) {
					saveDeviceData(device_number);
					Serial.print("Device #");
					Serial.print(device_number);
					Serial.println(" is SWITCHED ON");
					continue;
				}
			} else {
				Serial.println(String("Can't receive ack (timeout) from = ") + device_number);
			}
		}	// end if sendtoWait
		Serial.println(String("Can't send to wait to = ") + device_number);
		Serial.print("Device #");
		Serial.print(device_number);
		Serial.println(" is maybe SWITCHED OFF");
		_dataBase->setDeviceRFErr((char) device_number, true);
	} 	// end for
	Serial.println("RFManager::processDeveices() End");
}

void RFManager::setDataBase(DataBase* pDataBase) {
	_dataBase = pDataBase;
}

void RFManager::setEepromManager(EepromManager* pEepromMngr) {
	_eepromMngr = pEepromMngr;
}

/* 
* private methods
*
*/

void RFManager::init() {
	_driver = new RH_ASK(RADIO_FREG, RADIO_RX_PIN, RADIO_TX_PIN);//RH_Serial(Serial3);
	_radioMngr = new RHReliableDatagram(*_driver, BAZA_ADDRESS);
	_initError = !_radioMngr->init();
	_radioMngr->setThisAddress(BAZA_ADDRESS);
	_radioMngr->setHeaderFrom(BAZA_ADDRESS);//?????? why it is needed
}

bool RFManager::isDeviceKnown(uint8_t from, uint8_t* knownDeviceIds, uint8_t knownDeviceIdsCount) {
	_dataBase->fetchIds(knownDeviceIds);
	Serial.println(String("knownDeviceIdsCount") + knownDeviceIdsCount);//TEST
	for (int i = 0; i < knownDeviceIdsCount; i++) {
		if (knownDeviceIds[i] == from && dataInfoUnion.dataInfo._uniqID == getUniqID()) {
			Serial.println(String("knownDeviceIds[") + i + "] = " + knownDeviceIds[i]);//TEST
			Serial.println("BAZA: device was identified");//TEST
			return true;
		}               
	}
	return false;
}

long RFManager::getUniqID() {
	return _dataBase->getUniqBaseID();
	
}

void RFManager::prepareDataForKnowingTransmit(uint8_t pDeviceNumber) {
	dataInfoUnion.dataInfo._uniqID = getUniqID();
	dataInfoUnion.dataInfo._deviceID = pDeviceNumber;
}

void RFManager::prepareDataForWorkingTransmit(uint8_t pDeviceNumber) {
	dataInfoUnion.dataInfo._uniqID = getUniqID();
	dataInfoUnion.dataInfo._deviceID = pDeviceNumber;
	// dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue((char) pDeviceNumber); THAT IS RIGHT
	//TEST
	dataInfoUnion.dataInfo._deviceControl = blink[pDeviceNumber] ? 1.0 : 0.0;
	Serial.println(String(F("Control value = ")) + dataInfoUnion.dataInfo._deviceControl);	
	blink[pDeviceNumber] = !blink[pDeviceNumber];
	//TEST
}

void RFManager::saveDeviceData(uint8_t pDeviceNumber) {
	_dataBase->setDeviceAck((char) pDeviceNumber, dataInfoUnion.dataInfo._deviceAck);
	_dataBase->setDeviceAdj((char) pDeviceNumber, dataInfoUnion.dataInfo._adjustable);
	//_dataBase->setDeviceAdj(pDeviceNumber, dataInfoUnion.dataInfo._rotatable);	
}

void RFManager::addDeviceToDataBase(uint8_t pDeviceNumber) {
	_dataBase->addDeviceInfo((char) pDeviceNumber);
	_dataBase->setDeviceAdj((char) pDeviceNumber, dataInfoUnion.dataInfo._adjustable);
	//_dataBase->setDeviceAdj(pDeviceNumber, dataInfoUnion.dataInfo._rotatable);	
}
