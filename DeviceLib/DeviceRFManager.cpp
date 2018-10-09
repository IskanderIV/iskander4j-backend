// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#include "DeviceRFManager.h"
#include "DeviceDataBase.h"

uint8_t rfBuf[RH_NRF24_MAX_MESSAGE_LEN];

DeviceRFManager::DeviceRFManager(DeviceDataBase* pDataBase): 
						_driver(RADIO_CE_PIN, RADIO_SS_PIN), 
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
		initDataInfo();
		initRegInfo();
	} else {
		Serial.println(F("RFManager init failed"));
	}	
}

void DeviceRFManager::initDataInfo() {
	dataInfoUnion.dataInfo._index = 0;
	dataInfoUnion.dataInfo._boardUID = 0L;
	dataInfoUnion.dataInfo._deviceId = 1;
	dataInfoUnion.dataInfo._deviceAck = 0.0;
	dataInfoUnion.dataInfo._deviceControl = 0.0;
	dataInfoUnion.dataInfo._radioError = 0;
}

void DeviceRFManager::initRegInfo() {
	regInfoUnion.regInfo._index = 0;
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

void DeviceRFManager::sendInfo() {
	//Serial.println(F("DeviceRFManager::sendInfo() Begin"));
	if (_radioMngr.available()) {
        Serial.println(F("DEVICE: got something by RF!"));//TEST
        uint8_t from;
        // uint8_t len = sizeof(DataInfo);
        uint8_t len = sizeof(rfBuf);
		// Serial.println(String(F("begin receive time = ")) + millis());//TEST
        if (_radioMngr.recvfromAck(dataInfoUnion.byteBuffer, &len, &from) && isDataMessageForMe(from, dataInfoUnion.dataInfo._deviceId, dataInfoUnion.dataInfo._boardUID)) {
			float newControlData = dataInfoUnion.dataInfo._deviceControl;
			Serial.println(String(F("Received control value = ")) + newControlData); // TEST
			prepareDataForDataTransmit();
			if (_radioMngr.sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), from)) {
				updateControlFromBoard(newControlData);
				Serial.println(F("Good SendToWait. Update Control Data"));
			} else {
				Serial.println(F("SendToWait failed"));
			}
		}		
	}
	//Serial.println("DeviceRFManager::sendInfo() End");	
}

bool DeviceRFManager::identifyDevice() {
	uint8_t from;
	uint8_t len = sizeof(rfBuf); // TODO mayb needed buf here
	prepareDataForRegTransmit();
	// Serial.println(String(F("sizeof(regInfoUnion.byteBuffer) = ")) + String(len));
	
	if (_radioMngr.sendtoWait(regInfoUnion.byteBuffer, sizeof(RegInfo), BAZA_ADDRESS)) {
		Serial.println(String(F("good sendtoWait ")));
		// Now wait for a reply from Device		
		if (_radioMngr.recvfromAckTimeout(regInfoUnion.byteBuffer, &len, 1000, &from)) {
			// good radio connection
			updateStructureData();			
			return true;
		} else {
			Serial.println(F("Can't get Ack from Server ")); //TEST
		}
	} else {
		fixWrongRFConnection();
	}	
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
	uint8_t newDeviceId = regInfoUnion.regInfo._deviceId;
	long newBoardUID = regInfoUnion.regInfo._boardUID;
	
	Serial.println(String(F("New Device number is ")) + newDeviceId); //TEST
	Serial.println(String(F("New BoardUID is ")) + newBoardUID); //TEST
	
    _radioMngr.setThisAddress(newDeviceId);
    _radioMngr.setHeaderFrom(newDeviceId);
	
	_dataBase->setDeviceId(newDeviceId);
	_dataBase->setBoardUID(newBoardUID);
}

bool DeviceRFManager::isDataMessageForMe(uint8_t from, uint8_t deviceId, long boardUID) {	
	uint8_t passedDeviceId = deviceId;
	long passedBoardUID = boardUID;
	Serial.println(String(F("passedDeviceId = ")) + String(passedDeviceId));
	Serial.println(String(F("passedBoardUID = ")) + String(passedBoardUID));	
	
	uint8_t myId = _dataBase->getDeviceId();
	long myBoardUID = _dataBase->getBoardUID();
	Serial.println(String(F("myId = ")) + String(myId));
	Serial.println(String(F("myBoardUID = ")) + String(myBoardUID));
	
	if (from == BAZA_ADDRESS && myBoardUID == passedBoardUID && passedDeviceId == myId) {
			Serial.println(F("BAZA was identified"));//TEST
			return true;
	}
	return false;
}

void DeviceRFManager::updateControlFromBoard(float newControlData) {
	_dataBase->setDeviceControlValue(newControlData);
	Serial.println(String(F("Control value = ")) + _dataBase->getDeviceControlValue());
}

void DeviceRFManager::fixWrongRFConnection() {
	// here could be realised counter of failed connections
	Serial.println(F("Send Failed. RF connection is failed "));
}

//TODO avoid from duplication
void DeviceRFManager::prepareDataForRegTransmit() {
	uint8_t passedMsgIndex = regInfoUnion.regInfo._index;
	Serial.println(String(F("passedMsgIndex = ")) + String(passedMsgIndex));
	
	regInfoUnion.regInfo._boardUID = _dataBase->getBoardUID();
	regInfoUnion.regInfo._deviceId = _dataBase->getDeviceId();
	
	regInfoUnion.regInfo._min = _dataBase->getDeviceMin();
	regInfoUnion.regInfo._max = _dataBase->getDeviceMax();
	regInfoUnion.regInfo._discrete = _dataBase->getDeviceDiscrete();
	
	regInfoUnion.regInfo._digital = itob(_dataBase->getDeviceDigital());
	regInfoUnion.regInfo._analog = itob(_dataBase->getDeviceAnalog());
	regInfoUnion.regInfo._adjustable = itob(_dataBase->getDeviceAdj());
	regInfoUnion.regInfo._rotatable = itob(_dataBase->getDeviceRotatable());
	regInfoUnion.regInfo._radioError = itob(_dataBase->getDeviceRFErr());
}

void DeviceRFManager::prepareDataForDataTransmit() {
	uint8_t passedMsgIndex = dataInfoUnion.dataInfo._index;
	Serial.println(String(F("passedMsgIndex = ")) + String(passedMsgIndex));
	
	dataInfoUnion.dataInfo._boardUID = _dataBase->getBoardUID();
	dataInfoUnion.dataInfo._deviceId = _dataBase->getDeviceId();
	
	dataInfoUnion.dataInfo._deviceAck = _dataBase->getDeviceAck();
	
	dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue();
	
	// Serial.println(String("dataInfoUnion.dataInfo._boardUID = ") + dataInfoUnion.dataInfo._boardUID);
	// Serial.println(String("dataInfoUnion.dataInfo._deviceId = ") + dataInfoUnion.dataInfo._deviceId);
	// Serial.println(String("dataInfoUnion.dataInfo._deviceAck = ") + dataInfoUnion.dataInfo._deviceAck);
	// Serial.println(String("dataInfoUnion.dataInfo._deviceControl = ") + dataInfoUnion.dataInfo._deviceControl);
	// Serial.println(String("dataInfoUnion.dataInfo._adjustable = ") + String(dataInfoUnion.dataInfo._adjustable));
	// Serial.println(String("dataInfoUnion.dataInfo._rotatable = ") + String(dataInfoUnion.dataInfo._rotatable));
	// Serial.println(String("dataInfoUnion.dataInfo._radioError = ") + String(dataInfoUnion.dataInfo._radioError));
}