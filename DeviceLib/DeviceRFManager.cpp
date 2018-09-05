// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#include "DeviceRFManager.h"
#include "DeviceDataBase.h"

DeviceRFManager::DeviceRFManager(DeviceDataBase* pDataBase): 
						_driver(RADIO_FREG, RADIO_RX_PIN, RADIO_TX_PIN), 
						_radioMngr(_driver, 1),
						_dataBase(pDataBase) {
	init();
	Serial.println("DeviceRFManager()!");//TEST
}

DeviceRFManager::~DeviceRFManager() {
}

void DeviceRFManager::init() {
	_initError = !_radioMngr.init();
	if (!_initError) {
		uint8_t savedDeviceId = _dataBase->getDeviceId();
		_radioMngr.setThisAddress(savedDeviceId);
		_radioMngr.setHeaderFrom(savedDeviceId);		
	} else {
		Serial.println(F("Init RF error. Can't set Adresses and Headers from eeprom"));
	}	
}

/****************
* public methods*
*****************/

void DeviceRFManager::sendInfo() {
	//Serial.println(F("DeviceRFManager::sendInfo() Begin"));
	if (_radioMngr.available()) {
        Serial.println(F("DEVICE: got something by RF!!"));//TEST
        uint8_t from;
        uint8_t len = sizeof(DataInfo);
        if (_radioMngr.recvfromAck(dataInfoUnion.byteBuffer, &len, &from) && isDataMessageForMe(from)) {
			prepareDataForTransmit();
			if (_radioMngr.sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), from)) {
				// Good transmit and Ack
				updateControlFromBoard();					
				Serial.println(F("DeviceRFManager::searchDevice() good radio connection!"));										
			}			
		}
		// RF connection is failed (maybe transmiters and recievers on board and device sides)
		fixWrongRFConnection();
	}
	//Serial.println("DeviceRFManager::sendInfo() End");	
}

bool DeviceRFManager::identifyDevice() {
	uint8_t from;
	uint8_t len = sizeof(DataInfo); // TODO mayb needed buf here
	prepareDataForTransmit();
	Serial.println(String("sizeof(dataInfoUnion.byteBuffer) = ") + sizeof(dataInfoUnion.byteBuffer));
	
	if (_radioMngr.sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), BAZA_ADDRESS)) {
		// Now wait for a reply from Device		
		if (_radioMngr.recvfromAckTimeout(dataInfoUnion.byteBuffer, &len, 2000, &from)) {
			// good radio connection
			updateStructureData();
			Serial.print(String(F("New Device number is ")) + dataInfoUnion.dataInfo._deviceId);
			return true;
		}
	}
	// RF connection is failed (maybe transmiters and recievers on board and device sides)
	fixWrongRFConnection();
	return false;
}

/******************
* private methods *
*******************/


long DeviceRFManager::getBoardUID() {
	return _dataBase->getBoardUID();
}

// TODO control
void DeviceRFManager::updateStructureData() {
	uint8_t newDeviceId = dataInfoUnion.dataInfo._deviceId;
	uint8_t newBoardUID = dataInfoUnion.dataInfo._boardUID;
	
    _radioMngr.setThisAddress(newDeviceId);
    _radioMngr.setHeaderFrom(newDeviceId);
	
	_dataBase->setDeviceId(newDeviceId);
	_dataBase->setBoardUID(newBoardUID);
}

bool DeviceRFManager::isDataMessageForMe(uint8_t from) {
	uint8_t passedDeviceId = dataInfoUnion.dataInfo._deviceId;
	long passedBoardUID = dataInfoUnion.dataInfo._boardUID;
	
	uint8_t myId = _dataBase->getDeviceId();
	uint8_t myBoardUID = _dataBase->getBoardUID();
	
	if (from == BAZA_ADDRESS && myBoardUID == passedBoardUID && passedDeviceId == myId) {
			Serial.println(F("BAZA was identified"));//TEST
			return true;
	}
	return false;
}

void DeviceRFManager::updateControlFromBoard() {
	_dataBase->setDeviceControlValue(dataInfoUnion.dataInfo._deviceControl);
	Serial.println(String(F("Control value = ")) + _dataBase->getDeviceControlValue());
}

void DeviceRFManager::fixWrongRFConnection() {
	// here could be realised counter of failed connections
	Serial.println(F("Send Failed. RF connection is failed "));
}

//TODO avoid from duplication
void DeviceRFManager::prepareDataForTransmit() {
	dataInfoUnion.dataInfo._boardUID = _dataBase->getBoardUID();
	dataInfoUnion.dataInfo._deviceId = _dataBase->getDeviceId();
	dataInfoUnion.dataInfo._deviceAck = _dataBase->getDeviceAck();
	dataInfoUnion.dataInfo._min = _dataBase->getDeviceMin();
	dataInfoUnion.dataInfo._max = _dataBase->getDeviceMax();
	dataInfoUnion.dataInfo._discrete = _dataBase->getDeviceDiscrete();
	dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue();
	dataInfoUnion.dataInfo._digital = _dataBase->getDeviceDigital();
	dataInfoUnion.dataInfo._analog = _dataBase->getDeviceAnalog();
	dataInfoUnion.dataInfo._adjustable = _dataBase->getDeviceAdj();
	dataInfoUnion.dataInfo._rotatable = _dataBase->getDeviceRotatable();
	dataInfoUnion.dataInfo._radioError = _dataBase->getDeviceRFErr();
	Serial.println(String("dataInfoUnion.dataInfo._boardUID = ") + dataInfoUnion.dataInfo._boardUID);
	Serial.println(String("dataInfoUnion.dataInfo._deviceId = ") + dataInfoUnion.dataInfo._deviceId);
	Serial.println(String("dataInfoUnion.dataInfo._deviceAck = ") + dataInfoUnion.dataInfo._deviceAck);
	Serial.println(String("dataInfoUnion.dataInfo._deviceControl = ") + dataInfoUnion.dataInfo._deviceControl);
	Serial.println(String("dataInfoUnion.dataInfo._adjustable = ") + dataInfoUnion.dataInfo._adjustable);
	Serial.println(String("dataInfoUnion.dataInfo._rotatable = ") + dataInfoUnion.dataInfo._rotatable);
	Serial.println(String("dataInfoUnion.dataInfo._radioError = ") + dataInfoUnion.dataInfo._radioError);
}