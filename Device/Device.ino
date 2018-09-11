#include <DeviceController.h>
#include <DeviceButtonsManager.h>

DeviceButtonsManager* btnMngr;
DeviceController* controller;

void setup() {
  Serial.begin(9600);
  //Serial.begin(19200);//MINI2
  Serial.println("Before Constructors!");//TEST
  btnMngr = new DeviceButtonsManager();
  controller = new DeviceController(btnMngr);
  
  btnMngr->createButton(btn_CONTROLABLE, true);
  btnMngr->createButton(btn_ONN, true);
  btnMngr->createButton(btn_SEARCH, true);
  Serial.println(F("We have buttons!")); //TEST 
}

void loop() {
	controller->processLoop();
	delay(100);
}
