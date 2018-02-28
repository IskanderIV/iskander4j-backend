// Menu
// (c) Ivanov Aleksandr, 2018

#include "Menu.h"
#include "MenuNode.h"

Menu::Menu(){
	String rootName = MENU_ROOT_NAME;
	_root = new MenuNode(rootName, mn_Us);	
	_currActive = NULL;	
}

Menu::~Menu(){
	//TODO delete all Nodes and _root
}
	
bool Menu::addBefore(MenuNode* pMenuNode, MenuNode* pAddedNode){
	if (pMenuNode == NULL || pMenuNode == _root) return false;
	if (pAddedNode == NULL) return false;
	MenuNode* parent = pMenuNode->getParent();
	MenuNode* prev = pMenuNode->getPrev();
	pAddedNode->setNext(pMenuNode);
	pAddedNode->setPrev(prev);
	pMenuNode->setPrev(pAddedNode);
	pAddedNode->setParent(parent);
	// if pMenuNode is the first Node in list
	if (prev == NULL) {					
		parent->setFirstChild(pAddedNode);
	} else {			
		prev->setNext(pAddedNode);
	}
	parent->increaseChildCount();
	return true;
}

bool Menu::addAfter(MenuNode* pMenuNode, MenuNode* pAddedNode){
	if (pMenuNode == NULL || pMenuNode == _root) return false;
	if (pAddedNode == NULL) return false;
	MenuNode* parent = pMenuNode->getParent();
	MenuNode* next = pMenuNode->getNext();
	pAddedNode->setNext(next);
	pAddedNode->setPrev(pMenuNode);
	pMenuNode->setNext(pAddedNode);
	pAddedNode->setParent(parent);
	// if pMenuNode is the last Node in list
	if (next == NULL) {
		parent->setLastChild(pAddedNode);
	} else {
		next->setPrev(pAddedNode);
	}
	parent->increaseChildCount();
	return true;
}

bool Menu::addChildFirst(MenuNode* pParent, MenuNode* pAddedNode){	
	if (pParent->getFirstChild()) {
		return addBefore(pParent->getFirstChild(), pAddedNode);
	} else {
		pParent->setFirstChild(pAddedNode);
		pParent->setLastChild(pAddedNode);
		pAddedNode->setParent(pParent);
		pParent->increaseChildCount();
		return true;
	}
}

bool Menu::addChildLast(MenuNode* pParent, MenuNode* pAddedNode){
	if (pParent->getLastChild()) {
		return addAfter(pParent->getLastChild(), pAddedNode);
	} else {
		pParent->setFirstChild(pAddedNode);
		pParent->setLastChild(pAddedNode);
		pAddedNode->setParent(pParent);
		pParent->increaseChildCount();
		return true;
	}	
}

MenuNode* Menu::getRoot(){
	return _root;
}

MenuNode* Menu::getCurrActive(){
	return _currActive;
}

void Menu::setCurrActive(MenuNode* pCurrActive){
	_currActive = pCurrActive;
}

/*
MenuNode* addChildInPosition(MenuNode* pParent, int pPosition, MenuNode* pAddedNode){
	if (pParent == NULL) return NULL;
	if (pAddedNode == NULL) return pParent;
	MenuNode* tempNode = pMenuNode->getFirstChild();
	int currPos = 0;
	while(currPos != pPosition || tempNode) {
		currPos++;
		tempNode = tempNode->getNext();
	}
}
*/