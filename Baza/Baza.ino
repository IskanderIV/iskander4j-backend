#include <ButtonsManager.h>
#include <RFManager.h>
#include <EepromManager.h>
#include <WifiManager.h>
#include <DataBase.h>
#include <MenuBuilder.h>
#include <MenuSelector.h>
#include <MenuNode.h>
#include <Menu.h>
#include <Inputer.h>
#include <Chooser.h>
#include <HashMap.h>
#include <TwoStringLcdDisplay.h>
#include <Controller.h>

#define PWR_BTNS_PIN 51
#define GND_BTNS_PIN 33
#define DIODE_PLUS_BTNS_PIN 37
#define DIODE_MINUS_BTNS_PIN 35

MenuBuilder* menuBldr;
MenuSelector* menuSelector;
ButtonsManager* btnMngr;
RFManager* rfMngr;
EepromManager* eepromMngr;
WifiManager* wifiMngr;
DataBase* dataBase;
Inputer* inputer;
Chooser* chooser;
TwoStringLcdDisplay* lcd;
Controller* controller;

const unsigned int HASH_SIZE = 12;
HashMap<String, Action, HASH_SIZE> menuToActionMap = HashMap<String, Action, HASH_SIZE>();

void setup() {
  Serial.begin(9600);
  Serial.println("Before Constructors!");//TEST
  menuBldr = new MenuBuilder();
  menuSelector = new MenuSelector();
  btnMngr = new ButtonsManager();  
  eepromMngr = new EepromManager();
  dataBase = new DataBase(eepromMngr);
  wifiMngr = new WifiManager(9600, dataBase);
  rfMngr = new RFManager(dataBase);
  inputer = new Inputer();
  chooser = new Chooser();
  lcd = new TwoStringLcdDisplay();
  controller = new Controller();
  
  pinMode(PWR_BTNS_PIN, OUTPUT);
  digitalWrite(PWR_BTNS_PIN, HIGH);
  pinMode(GND_BTNS_PIN, OUTPUT);
  digitalWrite(GND_BTNS_PIN, LOW);
  pinMode(DIODE_MINUS_BTNS_PIN, OUTPUT);
  digitalWrite(DIODE_MINUS_BTNS_PIN, LOW);
  pinMode(DIODE_PLUS_BTNS_PIN, OUTPUT);
  digitalWrite(DIODE_PLUS_BTNS_PIN, HIGH);
  
  btnMngr->createButton(btn_menu, false);
  btnMngr->createButton(btn_left, false);
  btnMngr->createButton(btn_right, false);
  btnMngr->createButton(btn_up, false);
  btnMngr->createButton(btn_down, false);
  btnMngr->createButton(btn_select, false);
  Serial.println(F("We have buttons!")); //TEST
  
  String deviceSearchMenuName = F("Device Search");
  String regBoardMenuName = F("Register Board");
  String sttMenuName = F("Settings");
  String wifiCredsMenuName = F("WIFI");
  String wifiSearchMenuName = F("Wifi Search");
  String wifiSsidMenuName = F("Wifi SSID");
  String wifiPsswdMenuName = F("Wifi Psswd");
  String wifiResetMenuName = F("Wifi Reset");
  String siteMenuName = F("SITE");
  String tcpLoginMenuName = F("Login");
  String tcpPsswdMenuName = F("Password");
  String serverMenuName = F("SERVER");
  String hostMenuName = F("Host");
  String portMenuName = F("Port");
  String targetMenuName = F("Target");
  String boardMenuName = F("BOARD");
  String boardUIDMenuName = F("Board UID");
  
  menuBldr->addToMain(deviceSearchMenuName, mn_Action);
  menuToActionMap[deviceSearchMenuName] = act_SEARCH_DEVICES;
  
  menuBldr->addToMain(regBoardMenuName, mn_Action);
  menuToActionMap[regBoardMenuName] = act_REGISTER_BOARD;
  
  menuBldr->addToMain(sttMenuName, mn_Us);
  
  menuBldr->addTo(sttMenuName, wifiCredsMenuName, mn_Us);
  menuBldr->addTo(wifiCredsMenuName, wifiSearchMenuName, mn_Action);
  menuToActionMap[wifiSearchMenuName] = act_SELECT_WIFI;
  menuBldr->addTo(wifiCredsMenuName, wifiSsidMenuName, mn_Action);
  menuToActionMap[wifiSsidMenuName] = act_INPUT_WIFI_SSID;
  menuBldr->addTo(wifiCredsMenuName, wifiPsswdMenuName, mn_Action);
  menuToActionMap[wifiPsswdMenuName] = act_INPUT_WIFI_PSWD;
  menuBldr->addTo(wifiCredsMenuName, wifiResetMenuName, mn_Action);
  menuToActionMap[wifiResetMenuName] = act_RESET_WIFI;
  
  menuBldr->addTo(sttMenuName, siteMenuName, mn_Us);
  menuBldr->addTo(siteMenuName, tcpLoginMenuName, mn_Action);
  menuToActionMap[tcpLoginMenuName] = act_INPUT_TCP_LOGIN;
  menuBldr->addTo(siteMenuName, tcpPsswdMenuName, mn_Action);
  menuToActionMap[tcpPsswdMenuName] = act_INPUT_TCP_PSWD; 
  
  menuBldr->addTo(sttMenuName, serverMenuName, mn_Us);
  menuBldr->addTo(serverMenuName, hostMenuName, mn_Action);
  menuToActionMap[hostMenuName] = act_INPUT_SERVER_IP;
  menuBldr->addTo(serverMenuName, portMenuName, mn_Action);
  menuToActionMap[portMenuName] = act_INPUT_SERVER_PORT;
  menuBldr->addTo(serverMenuName, targetMenuName, mn_Action);
  menuToActionMap[targetMenuName] = act_INPUT_SERVER_TARGET;
  
  menuBldr->addTo(sttMenuName, boardMenuName, mn_Us);
  menuBldr->addTo(boardMenuName, boardUIDMenuName, mn_Action);
  menuToActionMap[boardUIDMenuName] = act_INPUT_BOARD_UID;
  
  menuSelector->setMenu(menuBldr->getMenu());
  menuSelector->setController(controller);
  Serial.println(F("We set menuSelector!"));//TEST
  
  lcd->setSelector(menuSelector);
  lcd->setInputer(inputer);
  lcd->setChooser(chooser);
  Serial.println(F("We set lcd!"));//TEST
  
  controller->setMenuSelector(menuSelector);
  controller->setDisplay(lcd);
  controller->setInputer(inputer);
  controller->setChooser(chooser);
  controller->setBtnManager(btnMngr);  
  controller->setWifiManager(wifiMngr);
  controller->setDataBase(dataBase);
  controller->setRFManager(rfMngr);
  controller->setMenuToActionMap(menuToActionMap);
  Serial.println(F("We set controller!"));//TEST
  Serial.println(F("We set dataBase!"));//TEST 
  Serial.println(F("We set wifi!"));//TEST
  Serial.println(F("We set rf!"));//TEST
}

void loop() {
//  lcd->printHeader("HELLO");
//  lcd->printInfo("INFO");
  controller->processLoop();
  //Serial.println("We are in loop!");
  delay(100);
}
