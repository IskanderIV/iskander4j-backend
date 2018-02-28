// Inputer
// (c) Ivanov Aleksandr, 2018

#ifndef _Inputer_H_
#define _Inputer_H_

#include "ControllerInputerObserver.h"
#include "DisplayInputerObserver.h"

#define SPACE_SYMBOL 32
//#define DEBUG
// class ControllerInputerObserver;
class Event;

class Inputer : public ControllerInputerObserver, public DisplayInputerObserver
{
public:
	Inputer();
	~Inputer();
	
	// interface impl for controllers events
	virtual void moveCursorLeft();
	virtual void moveCursorRight();
	virtual void changeSymbolUp();
	virtual void changeSymbolDown();
	virtual void saveText();
	virtual void clear(Event* _event);
	virtual String getSavedText();
	
	// interface impl for display
	virtual char getCurrSymbol();
	virtual int getCurrPosition();
	virtual const char* getCurrString();
	
private:
	int	_currPosition;
	char _currSymbol;
	const char* _symbols;
	char* _inputText;
	String _savedText;
	int _currInputTextLength;
	
	int getCurrLastPosition();
};

#endif