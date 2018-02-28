// Menu
// (c) Ivanov Aleksandr, 2018

#ifndef _Menu_H_
#define _Menu_H_

//#define DEBUG // Defined to reduce code size. Disable all debugging!

class MenuNode;

#define MENU_ROOT_NAME "root"

class Menu
{
public:
	Menu();
	~Menu();
	
	bool addBefore(MenuNode* _menuNode, MenuNode* _addedNode);
	bool addAfter(MenuNode* _menuNode, MenuNode* _addedNode);
	bool addChildFirst(MenuNode* _parent, MenuNode* _addedNode);
	bool addChildLast(MenuNode* _parent, MenuNode* _addedNode);
	bool addChildInPosition(MenuNode* _parent, int pPosition, MenuNode* _addedNode);
	MenuNode* getRoot();
	MenuNode* getCurrActive();
	void setCurrActive(MenuNode* _currActive);
	
private:	
	MenuNode* _currActive;
	MenuNode* _root;
};

#endif