package ru.cleverhause.it.arduino;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.cleverhause.it.request.ArduinoRequest;
import ru.cleverhause.it.request.ArduinoRequestBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Collections;

public class BordsConnectionIT {
    private static final Logger logger = Logger.getLogger(BordsConnectionIT.class);

    @Before
    public void setUp() {
        ArduinoRequest arduinoRequest = ArduinoRequestBuilder.create()
                .setBody(new JSONPObject("", null))
                .setHeaders(Collections.emptyMap())
                .build();


        try(Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress("localhost", 8090);
//            socket.bind(socketAddress);
            socket.connect(socketAddress);
            SocketChannel socketChannel = socket.getChannel();
            if (socketChannel.isConnected()) {
                logger.debug("get connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void successAnswerForInitReqTest() {

    }



}
