// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#include "DeviceRFManager.h"
#include "DeviceDataBase.h"


DeviceRFManager::DeviceRFManager(): _driver(RADIO_FREG, RADIO_RX_PIN, RADIO_TX_PIN), _radioMngr(_driver, INIT_ADRESS) {
	init();
	Serial.println("DeviceRFManager()!");//TEST
}

DeviceRFManager::~DeviceRFManager() {	
}

/* 
* interface impl for controllers requests 
*
*/

void DeviceRFManager::sendInfo(){
	//Serial.println(F("DeviceRFManager::sendInfo() Begin"));
	if (_radioMngr.available()) {
        Serial.println(F("DEVICE: got something by RF!!"));//TEST
        uint8_t from;
        uint8_t len = sizeof(DataInfo);
        if (_radioMngr.recvfromAck(dataInfoUnion.byteBuffer, &len, &from)) {
			if (isRightUniqIdAndFrom(from, getUniqID())) {
				_dataBase->setDeviceControlValue(dataInfoUnion.dataInfo._deviceControl);
				Serial.println(String(F("Control value = ")) + dataInfoUnion.dataInfo._deviceControl);
				prepareDataForWorkingTransmit();
				if (_radioMngr.sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), from)) {
					// Good transmit and Ack					
					Serial.println(F("DeviceRFManager::searchDevice() GOOD End"));										
				} else {
					// Bad transmit or Ack. Problem with BASE
					dataInfoUnion.dataInfo._radioError = true;
					_dataBase->setDeviceRFErr(true);//TODO after solving problems with rf replace that to controller
					Serial.println(F("sendtoWait failed. Problem with BASE"));
				}
			} // end of if (!isRightUniqIdAndFrom...
			
		} else {
			//Problem with MY Transmitter
			dataInfoUnion.dataInfo._radioError = true;
			_dataBase->setDeviceRFErr(true);
			Serial.println(F("recvfromAck failed. Problem with MY Transmitter"));
		}
	}
	//Serial.println("DeviceRFManager::sendInfo() End");	
}

bool DeviceRFManager::identifyDevice(){
	Serial.println(F("DeviceRFManager::identifyDevice() Begin"));
	prepareDataForKnowingTransmit();
	uint8_t from;
	uint8_t len = sizeof(DataInfo);
	Serial.println(String("sizeof(dataInfoUnion.byteBuffer) = ") + sizeof(dataInfoUnion.byteBuffer));
	Serial.println(String("sizeof(DataInfo) = ") + sizeof(DataInfo));
	if (_radioMngr.sendtoWait(dataInfoUnion.byteBuffer, sizeof(DataInfo), BAZA_ADDRESS)) {
		// Now wait for a reply from Device		
		if (_radioMngr.recvfromAckTimeout(dataInfoUnion.byteBuffer, &len, 4000, &from)) {
			Serial.print("Get data from BAZZA "); Serial.println(from);
			setNewAdressAndHeadersInfo(dataInfoUnion.dataInfo._deviceID);
			Serial.print("New Device number is ");
			Serial.print(dataInfoUnion.dataInfo._deviceID);
			return true;
		} else {
			dataInfoUnion.dataInfo._radioError = true;
			_dataBase->setDeviceRFErr(true);
			Serial.println(F("recvfromAck failed. Problem with MY Transmitter"));
		}
	} else {
		dataInfoUnion.dataInfo._radioError = true;
		_dataBase->setDeviceRFErr(true);
		Serial.println(F("sendtoWait failed. Problem with BASE RF"));
	}
	Serial.print(F("DeviceRFManager::identifyDevice() End"));
	return false;
}

void DeviceRFManager::setDataBase(DeviceDataBase* pDataBase) {
	_dataBase = pDataBase;
	if (!_initError) {
		uint8_t rememberedDeviceID = _dataBase->getDeviceID();
		setNewAdressAndHeadersInfo(rememberedDeviceID);		
	} else {
		Serial.println(F("Init rf error. Problem with setting Adresses and Headers"));
	}
}

void DeviceRFManager::setNewAdressAndHeadersInfo(uint8_t deviceNumber) {
  _radioMngr.setThisAddress(deviceNumber);
  _radioMngr.setHeaderFrom(deviceNumber);
}

/* 
* private methods
*
*/

void DeviceRFManager::init() {
	_initError = !_radioMngr.init();	
}

bool DeviceRFManager::isRightUniqIdAndFrom(uint8_t from, long pUniqID) {
	long savedUniqID = _dataBase->getUniqBaseID();
	if (from == BAZA_ADDRESS && pUniqID == savedUniqID) {
			Serial.println(F("BAZA was identified"));//TEST
			return true;
	}
	return false;
}

void DeviceRFManager::setUniqID(long pUniqID) {
	dataInfoUnion.dataInfo._uniqID = pUniqID;	
}

long DeviceRFManager::getUniqID() {
	return dataInfoUnion.dataInfo._uniqID;	
}

void DeviceRFManager::setDeviceID(uint8_t pDeviceID) {
	dataInfoUnion.dataInfo._deviceID = pDeviceID;	
}

uint8_t DeviceRFManager::getDeviceID() {
	return dataInfoUnion.dataInfo._deviceID;	
}
//TODO avoid from duplication
void DeviceRFManager::prepareDataForKnowingTransmit() {
	dataInfoUnion.dataInfo._uniqID = _dataBase->getUniqBaseID();
	dataInfoUnion.dataInfo._deviceID = _dataBase->getDeviceID();
	dataInfoUnion.dataInfo._deviceAck = _dataBase->getDeviceAck();
	dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue();
	dataInfoUnion.dataInfo._adjustable = _dataBase->isDeviceAdj();
	dataInfoUnion.dataInfo._rotatable = _dataBase->isDeviceRot();
	dataInfoUnion.dataInfo._radioError = _dataBase->isDeviceRFErr();
	Serial.println(String("dataInfoUnion.dataInfo._uniqID = ") + dataInfoUnion.dataInfo._uniqID);
	Serial.println(String("dataInfoUnion.dataInfo._deviceID = ") + dataInfoUnion.dataInfo._deviceID);
	Serial.println(String("dataInfoUnion.dataInfo._deviceAck = ") + dataInfoUnion.dataInfo._deviceAck);
	Serial.println(String("dataInfoUnion.dataInfo._deviceControl = ") + dataInfoUnion.dataInfo._deviceControl);
	Serial.println(String("dataInfoUnion.dataInfo._adjustable = ") + dataInfoUnion.dataInfo._adjustable);
	Serial.println(String("dataInfoUnion.dataInfo._rotatable = ") + dataInfoUnion.dataInfo._rotatable);
	Serial.println(String("dataInfoUnion.dataInfo._radioError = ") + dataInfoUnion.dataInfo._radioError);
}

void DeviceRFManager::prepareDataForWorkingTransmit() {
	dataInfoUnion.dataInfo._uniqID = _dataBase->getUniqBaseID();
	dataInfoUnion.dataInfo._deviceID = _dataBase->getDeviceID();
	dataInfoUnion.dataInfo._deviceAck = _dataBase->getDeviceAck();
	dataInfoUnion.dataInfo._deviceControl = _dataBase->getDeviceControlValue();
	dataInfoUnion.dataInfo._adjustable = _dataBase->isDeviceAdj();
	dataInfoUnion.dataInfo._rotatable = _dataBase->isDeviceRot();
	dataInfoUnion.dataInfo._radioError = _dataBase->isDeviceRFErr();
	Serial.println(String("dataInfoUnion.dataInfo._uniqID = ") + dataInfoUnion.dataInfo._uniqID);
	Serial.println(String("dataInfoUnion.dataInfo._deviceID = ") + dataInfoUnion.dataInfo._deviceID);
	Serial.println(String("dataInfoUnion.dataInfo._deviceAck = ") + dataInfoUnion.dataInfo._deviceAck);
	Serial.println(String("dataInfoUnion.dataInfo._deviceControl = ") + dataInfoUnion.dataInfo._deviceControl);
	Serial.println(String("dataInfoUnion.dataInfo._adjustable = ") + dataInfoUnion.dataInfo._adjustable);
	Serial.println(String("dataInfoUnion.dataInfo._rotatable = ") + dataInfoUnion.dataInfo._rotatable);
	Serial.println(String("dataInfoUnion.dataInfo._radioError = ") + dataInfoUnion.dataInfo._radioError);
}
