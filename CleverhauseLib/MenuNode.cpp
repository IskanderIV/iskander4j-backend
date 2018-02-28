// MenuNode
// (c) Ivanov Aleksandr, 2018

#include "MenuNode.h"

MenuNode::MenuNode(String& title, MenuNodeType pMnType){
	_title = title;
	_mnType = pMnType;
	_active = false;
	_parent = NULL;
	_firstChild = NULL;
	_lastChild = NULL;
	_next = NULL;
	_prev = NULL;
	_childCount = 0;
}

MenuNode::~MenuNode(){}

MenuNode* MenuNode::getParent(){
	return _parent;
}

void MenuNode::setParent(MenuNode* pParanet) {
	_parent = pParanet;
}

MenuNode* MenuNode::getNext() {
	return _next;
}

void MenuNode::setNext(MenuNode* pNext) {
	_next = pNext;
}

MenuNode* MenuNode::getPrev() {
	return _prev;
}

void MenuNode::setPrev(MenuNode* pPrev) {
	_prev = pPrev;
}

MenuNode* MenuNode::getFirstChild() {
	return _firstChild;
}

void MenuNode::setFirstChild(MenuNode* pFirstChild) {
	_firstChild = pFirstChild;
}

MenuNode* MenuNode::getLastChild() {
	return _lastChild;
}

void MenuNode::setLastChild(MenuNode* pLastChild){
	_lastChild = pLastChild;
}

boolean MenuNode::isActive(){
	return _active;
}

void MenuNode::setActive(boolean pActive){
	_active = pActive;
}

int MenuNode::getChildCount(){
	return _childCount;
}

void MenuNode::setChildCount(int pChildCount){
	_childCount = pChildCount;
}

void MenuNode::increaseChildCount(){
	_childCount++;
}

void MenuNode::decreaseChildCount(){
	_childCount--;
}

String MenuNode::getTitle(){
	return _title;
}

void MenuNode::setTitle(String& pTitle){
	_title = pTitle;
}

MenuNodeType MenuNode::getType(){
	return _mnType;
}

// boolean MenuNode::isTextual(){
	// if (_mnType == mn_Txt) {
		// return true;
	// } else {
		// return false;
	// }
// }

boolean MenuNode::isActionNode(){
	if (_mnType == mn_Action) {
		return true;
	} else {
		return false;
	}
}

// boolean MenuNode::isChoosingAction(){
	// if (_mnType == mn_Chooser) {
		// return true;
	// } else {
		// return false;
	// }
// }