// TwoStringLcdDisplay
// (c) Ivanov Aleksandr, 2018

#ifndef _TwoStringLcdDisplay_H_
#define _TwoStringLcdDisplay_H_

#define LCD_ROW_NUM 2
#define LCD_COL_NUM 16
#define LCD_ROW_1 0
#define LCD_ROW_2 1
#define EMPTY_STRING "                "
#define MENU_HEADER_STRING "Menu"
#define INPUTER_HEADER_STRING "Input"
#define CHOOSER_HEADER_STRING "Choose"

#include "ControllerDisplayObserver.h"

//#define DEBUG
// class ControllerDisplayObserver;
class DisplaySelectorObserver;
class DisplayInputerObserver;
class DisplayChooserInterface;
class LiquidCrystal_I2C;

class TwoStringLcdDisplay : public ControllerDisplayObserver
{
public:
	TwoStringLcdDisplay();
	~TwoStringLcdDisplay();
	
	// interface impl for controllers events
	virtual void showMenu();
	virtual void showCurrMenu();
	virtual void hideMenu();
	virtual void showInitRF();
	virtual void showInitGSM();
	virtual void showInputer();
	virtual void showCurrInputSymbol();
	virtual void showChooser();
	virtual void showCurrChooserElement();
	
	void showConnectionToWifiMsg(bool _isDone);
	
	// useful lcd methods
	void init();
	void printHeader(String& _str); // row == 1
	void printInfo(String& _str); // row == 2
	void printInfoSymbol(char _symbol, int _position); // row == 2
	void clearScreen();
	void clearInfo();
	void setSelector(DisplaySelectorObserver* _menuSelector);
	void setInputer(DisplayInputerObserver* _inputer);
	void setChooser(DisplayChooserInterface* pChooser);
	
private:
	LiquidCrystal_I2C* 			_lcd;
	DisplaySelectorObserver* 	_menuSelector;
	DisplayInputerObserver* 	_inputer;
	DisplayChooserInterface* 	_chooser;
	
	void clearRegion(int row, int col1, int col2);
};

#endif