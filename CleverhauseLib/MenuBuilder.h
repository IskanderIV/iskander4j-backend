// MenuBuilder
// (c) Ivanov Aleksandr, 2018

#ifndef _MenuBuilder_H_
#define _MenuBuilder_H_

#include "MenuNode.h"

//#define DEBUG // Defined to reduce code size. Disable all debugging!

class Menu;
class MenuNode;

class MenuBuilder
{
public:
	MenuBuilder();
	~MenuBuilder();	
	
	boolean addTo(String& _parentName, String& _childName, MenuNodeType _mnType);
	void addToMain(String& _nodeName, MenuNodeType _mnType);
	Menu* getMenu();
	
private:
	Menu* _menu;
	
	MenuNode* find(String& _menuNodeName);
	MenuNode* findIn(String& _menuNodeName, MenuNode* _node);
	void createMenu();
};

#endif