// Event
// (c) Ivanov Aleksandr, 2018

#include "Event.h"

Event::Event(String& pPayload) {
	_payload = pPayload;
}

Event::~Event() {	
}
	
String Event::getPayload() {
	return _payload;
}

void Event::setPayload(String& pPayload) {
	_payload = pPayload;
}
