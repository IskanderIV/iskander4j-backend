package ru.cleverhause.it.arduino;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.cleverhause.response.HttpResponse;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.DeviceInfo;
import ru.cleverhause.util.TestJsonUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static ru.cleverhause.util.HttpUtil.doPost;

public class BoardsConnectionIT {
    public static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final Logger logger = Logger.getLogger(BoardsConnectionIT.class);

    @Before
    public void setUp() {
    }

    @Test()
    public void successAnswerForInitReqTest() throws MalformedURLException, JsonProcessingException {
        URL localhost = new URL("http", "localhost", 8090, "/cleverhause/arduino/data");
        Headers headers = Headers.of(
                "cookie", "1"
        );
        BoardReq boardReq = new BoardReq(Arrays.asList(
                new DeviceInfo(1, 0.7, false, 0.0, false),
                new DeviceInfo(2, 0.6, false, 0.0, false),
                new DeviceInfo(3, 0.5, true, 0.7, true)
        ));
        RequestBody jsonBody = RequestBody.create(JSON_CONTENT_TYPE, TestJsonUtil.TEST_OBJECT_MAPPER.writeValueAsBytes(boardReq));
        Request req = new Request.Builder()
                .url(localhost)
                .headers(headers)
                .post(jsonBody)
                .build();
        try {
            HttpResponse response = doPost(req);
            Assert.assertEquals(200, response.getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
