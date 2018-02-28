// DeviceController
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceController_H_
#define _DeviceController_H_

#include "DeviceButtonsManager.h"
#include "DeviceEepromManager.h"

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

//#define DEBUG
class DeviceButtonsManager;
class DeviceEepromManager;
class DeviceRFManager;
class DeviceDataBase;
class ActuatorInterface;
class DeviceSensor;
class Signalisator;

class DeviceController
{
private:
	DeviceButtonsManager* 	_btnManager;
	DeviceEepromManager* 	_eepromManager;
	DeviceRFManager* 		_rfManager;
	DeviceDataBase* 		_dataBase;
	ActuatorInterface* 		_actuator;
	DeviceSensor* 			_sensor;
	Signalisator* 			_signalisator;
	
	bool _workState;
	bool _searchState;
	
public:
	DeviceController();
	~DeviceController();
	
	void processLoop();
	
	//setters
	void setBtnManager(DeviceButtonsManager* _btnManager);
	void setEepromManager(DeviceEepromManager* _eepromManager);
	void setRFManager(DeviceRFManager* _rfManager);
	void setDataBase(DeviceDataBase* _dataBase);
	void setActuator(ActuatorInterface* _actuator);
	void setDeviceSensor(DeviceSensor* _sensor);
	void setSignalisator(Signalisator* _signalisator);
	
private:	
	void init();
	void doWork();
	void doIdentify();
	//void saveInputTextToEeprom(String& _activeMenuName, String& _inputText);
	//EepromPlaceName mapActionToEepromPlace(Action);
	void processButtons();
	//void searhDevices();
};

#endif