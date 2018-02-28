// Object
// (c) Ivanov Aleksandr, 2018

#include "Object.h"

Object::Object() {
	_active = false;
}

Object::~Object() {
}

boolean Object::isActive() {
	return _active;
}

void Object::setActive(boolean pActive) {
	_active = pActive;
}