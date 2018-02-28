#include <DeviceRFManager.h>
#include <DeviceEepromManager.h>
#include <DeviceDataBase.h>
#include <DeviceController.h>
#include <DeviceButtonsManager.h>
#include <DeviceDigitalActuator.h>
#include <DeviceSensor.h>
#include <Signalisator.h>

DeviceButtonsManager* btnMngr;
DeviceRFManager* rfMngr;
DeviceEepromManager* eepromMngr;
DeviceDataBase* dataBase;
DeviceController* controller;
DeviceDigitalActuator* digitalActuator;
DeviceSensor* sensor;
Signalisator* signalisator;

void setup() {
  Serial.begin(9600);
  //Serial.begin(19200);//MINI2
  Serial.println("Before Constructors!");//TEST
  btnMngr = new DeviceButtonsManager();  
  eepromMngr = new DeviceEepromManager();
  dataBase = new DeviceDataBase();
  rfMngr = new DeviceRFManager();
  controller = new DeviceController();
  digitalActuator = new DeviceDigitalActuator();
  sensor = new DeviceSensor();
  signalisator = new Signalisator();
  
  btnMngr->createButton(btn_CONTROLABLE, true);
  btnMngr->createButton(btn_ONN, true);
  btnMngr->createButton(btn_SEARCH, true);
  Serial.println(F("We have buttons!")); //TEST
  
  controller->setBtnManager(btnMngr);
  controller->setEepromManager(eepromMngr);
  controller->setRFManager(rfMngr);
  controller->setDataBase(dataBase);
  controller->setActuator(digitalActuator);
  controller->setDeviceSensor(sensor);
  controller->setSignalisator(signalisator);
  Serial.println(F("We have controller!")); //TEST
  
  dataBase->setEepromManager(eepromMngr); //Need to be after controller sets. //That operation must be on controller side
  Serial.println(F("We have dataBase!")); //TEST
  
  rfMngr->setDataBase(dataBase); //Need to be after dataBase sets
  Serial.println(F("We have rfManager!")); //TEST  
}

void loop() {
	controller->processLoop();
	delay(100);
}