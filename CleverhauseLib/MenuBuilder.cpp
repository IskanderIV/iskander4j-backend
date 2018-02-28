// MenuBuilder
// (c) Ivanov Aleksandr, 2018

#include "Menu.h"

#include "MenuBuilder.h"

MenuBuilder::MenuBuilder(){
	createMenu();
	Serial.println("MenuBuilder()!");//TEST
}
MenuBuilder::~MenuBuilder(){
}

void MenuBuilder::createMenu() {
	_menu = new Menu();
}

MenuNode* MenuBuilder::find(String& pMenuNodeName) {
	MenuNode* temp = findIn(pMenuNodeName, _menu->getRoot());	
	return temp;
}

MenuNode* MenuBuilder::findIn(String& pMenuNodeName, MenuNode* pNode) {
#ifdef DEBUG
	Serial.println(pNode->getTitle());
#endif
	MenuNode* temp = pNode;
	while(temp) {		
		//delay(2000);
		if (strncmp(temp->getTitle().c_str(), pMenuNodeName.c_str(), pMenuNodeName.length()) == 0) {
#ifdef DEBUG
			Serial.print("Node finded >> "); Serial.println(temp->getTitle());
#endif
			return temp;
		}
		if (temp->getFirstChild()) {
			temp = temp->getFirstChild();
#ifdef DEBUG
			Serial.print("Next node is FirstChild>> "); Serial.println(temp->getTitle());
#endif
		} else if (temp->getNext()) {
			temp = temp->getNext();
#ifdef DEBUG
			Serial.print("Next node is Next>> "); Serial.println(temp->getTitle());
#endif
		} else if (temp->getParent()->getNext()) {
			temp = temp->getParent()->getNext();
#ifdef DEBUG
			Serial.print("Next node is getParent()->getNext()>> "); Serial.println(temp->getTitle());
#endif
		} else {
			temp = NULL;
#ifdef DEBUG
			Serial.print("Next node >> NULL"); Serial.println("NULL");
#endif
		}
#ifdef DEBUG
		Serial.println("Find cicle");
#endif
	}
#ifdef DEBUG
	Serial.println("Node is NOT finded");
#endif
	return NULL;
}

boolean MenuBuilder::addTo(String& pParentName, String& pChildName, MenuNodeType pMnType) {
	MenuNode* temp = find(pParentName);
	if (temp) {
		MenuNode* addedNode = new MenuNode(pChildName, pMnType);
		_menu->addChildLast(temp, addedNode);
		return true;
	} else {
		return false;
	}	
}

void MenuBuilder::addToMain(String& pNodeName, MenuNodeType pMnType) {
	MenuNode* addedNode = new MenuNode(pNodeName, pMnType);
	_menu->addChildLast(_menu->getRoot(), addedNode);	
}

Menu* MenuBuilder::getMenu() {
	return _menu;
}

