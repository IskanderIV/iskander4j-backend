// RequestBuilder.cpp
// (c) Ivanov Aleksandr, 2018

RequestBuilder::RequestBuilder()
{
	init();
	Serial.println("RequestBuilder()!");//TEST
}

RequestBuilder::~RequestBuilder(){
	//delete _wifi;
	//delete _httpConnection;
}