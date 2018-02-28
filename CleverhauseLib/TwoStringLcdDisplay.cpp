// TwoStringLcdDisplay
// (c) Ivanov Aleksandr, 2018

#include "TwoStringLcdDisplay.h"
#include "DisplaySelectorObserver.h"
#include "DisplayInputerObserver.h"
#include "DisplayChooserInterface.h"
#include "Chooser.h"
#include <LiquidCrystal_I2C.h>
#include <Wire.h>

TwoStringLcdDisplay::TwoStringLcdDisplay() {	
	init();
	Serial.println("LCD()!");//TEST
}

TwoStringLcdDisplay::~TwoStringLcdDisplay(){
	delete _lcd;
}

void TwoStringLcdDisplay::init() {
	_lcd = new LiquidCrystal_I2C(0xbf, LCD_COL_NUM, LCD_ROW_NUM);
	_lcd->init();
	_lcd->backlight();
}

/* 
* interface impl for controllers events 
*
*/

void TwoStringLcdDisplay::showMenu(){
	_lcd->cursor_off();
	clearScreen();
	String str;
	if (_menuSelector) {
		str = MENU_HEADER_STRING;		
	} else {
		str = "No Menu";
	}
	printHeader(str);
}

void TwoStringLcdDisplay::showCurrMenu(){
	_lcd->cursor_off();
	clearInfo();
	if (_menuSelector) {
		String str = _menuSelector->getCurrMenuName();
		printInfo(str);
	}		
}

void TwoStringLcdDisplay::hideMenu(){
	clearScreen();
}

void TwoStringLcdDisplay::showInputer(){
	_lcd->cursor_on();
	clearScreen();
	String str;	
	if (_inputer) {
		str = INPUTER_HEADER_STRING;		
	} else {
		str = "No Inputer";
	}
	printHeader(str);
}

void TwoStringLcdDisplay::showCurrInputSymbol(){
	//clearScreen();
	if (_inputer) {
		printInfoSymbol(_inputer->getCurrSymbol(), _inputer->getCurrPosition());
		// Serial.print("Curr symbol >>> ");  Serial.println(_inputer->getCurrSymbol());//TEST
		// Serial.print("Curr position >>> ");  Serial.println(_inputer->getCurrPosition());//TEST
	}
	//lcd.showCursor(_inputer->getCurrPosition()); // check wether pos less than LCD_COL_NUM
}

void TwoStringLcdDisplay::showChooser(){
	_lcd->cursor_off();
	clearScreen();
	String str;	
	if (_chooser) {
		str = CHOOSER_HEADER_STRING;		
	} else {
		str = "No Chooser";
	}
	printHeader(str);
}

void TwoStringLcdDisplay::showCurrChooserElement(){
	clearInfo();
	if (_chooser) {
		String str = _chooser->getCurrElement();
		printInfo(str);
		Serial.print("TwoStringLcdDisplay::showCurrChooserElement Curr Element >>> ");  Serial.println(_chooser->getCurrElement());//TEST
	}
}

void TwoStringLcdDisplay::showInitRF(){
	
}

void TwoStringLcdDisplay::showInitGSM(){
	
}

void TwoStringLcdDisplay::showConnectionToWifiMsg(bool isDone){
	String wifiConHeader = F("WIFI CONNECT");
	printHeader(wifiConHeader);
	String wifiConInfo;
	if (isDone) {
		wifiConInfo = F("is failed!");
	} else {
		wifiConInfo = F("has got!");
	}
	printInfo(wifiConInfo);
}

/* 
* useful lcd methods
*
*/

void TwoStringLcdDisplay::printHeader(String& pStr) {
	_lcd->setCursor(0, LCD_ROW_1);
	_lcd->print(pStr);
}

void TwoStringLcdDisplay::printInfo(String& pStr) {
	if (!_lcd) return;
	_lcd->setCursor(0, LCD_ROW_2);  
	_lcd->print(pStr);
}

void TwoStringLcdDisplay::printInfoSymbol(char pSymbol, int pPosition) {
	if (!_lcd) return;
	// if (pPosition > LCD_COL_NUM) //TODO
	_lcd->setCursor(pPosition, LCD_ROW_2);  
	_lcd->print(pSymbol);
	// Serial.print("TwoStringLcdDisplay::printInfoSymbol _currPosition >>> "); Serial.println(pPosition);//TEST
	// Serial.print("TwoStringLcdDisplay::printInfoSymbol _currSymbol >>> "); Serial.println(pSymbol);//TEST
}

void TwoStringLcdDisplay::clearScreen() {
	if (!_lcd) return;
	_lcd->setCursor(0, LCD_ROW_1);
	_lcd->printstr(EMPTY_STRING);
	_lcd->setCursor(0, LCD_ROW_2);
	_lcd->printstr(EMPTY_STRING);
}

void TwoStringLcdDisplay::clearInfo() {
	if (!_lcd) return;
	_lcd->setCursor(0, LCD_ROW_2);
	_lcd->printstr(EMPTY_STRING);
}

void TwoStringLcdDisplay::clearRegion(int row, int col1, int col2) {
  if (row < 0 || row > LCD_ROW_NUM - 1) return;
  if (col1 > col2) return;
  if (col1 < 0 || col1 > LCD_COL_NUM - 1) return;
  if (col2 < 0 || col2 > LCD_COL_NUM - 1) return;
  String clear_text = "";
  for (int i = 0; i < col2 - col1 + 1; i++) {
    clear_text += (char)0x20;
  }
  _lcd->setCursor(col1, row);
  _lcd->print(clear_text);
}

void TwoStringLcdDisplay::setSelector(DisplaySelectorObserver* pMenuSelector) {
	_menuSelector = pMenuSelector;
}

void TwoStringLcdDisplay::setInputer(DisplayInputerObserver* pInputer) {
	_inputer = pInputer;
}

void TwoStringLcdDisplay::setChooser(DisplayChooserInterface* pChooser) {
	_chooser = pChooser;
}