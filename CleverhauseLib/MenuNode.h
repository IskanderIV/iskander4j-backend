// MenuNode
// (c) Ivanov Aleksandr, 2018

#ifndef _MenuNode_H_
#define _MenuNode_H_

//#define DEBUG

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

enum MenuNodeType {
	mn_Us,
	mn_Action
};

class MenuNode
{
public:
	MenuNode(String& _title, MenuNodeType _mnType);
	~MenuNode();
	
	String getTitle();
	void setTitle(String& _title);
	MenuNodeType getType();
	MenuNode* getParent();
	void setParent(MenuNode* _parent);
	MenuNode* getNext();
	void setNext(MenuNode* _next);
	MenuNode* getPrev();
	void setPrev(MenuNode* _prev);
	MenuNode* getFirstChild();
	void setFirstChild(MenuNode* _firstChild);
	MenuNode* getLastChild();
	void setLastChild(MenuNode* _lastChild);
	boolean isActive();
	void setActive(boolean _active);
	int getChildCount();
	void setChildCount(int _childCount);
	void increaseChildCount();
	void decreaseChildCount();
	//boolean isTextual();
	boolean isActionNode();
	//boolean isChoosingAction();
	
private:
	String		_title;
	MenuNode* 	_parent;
	MenuNode* 	_firstChild;
	MenuNode* 	_lastChild;
	MenuNode* 	_next;
	MenuNode* 	_prev;
	boolean 	_active;
	MenuNodeType 	_mnType;
	int 		_childCount;
};

#endif