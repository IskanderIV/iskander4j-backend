package ru.cleverhause.it.arduino;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.cleverhause.rest.HttpResponse;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceInfo;
import ru.cleverhause.util.JsonUtil;
import ru.cleverhause.util.TestJsonUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static ru.cleverhause.util.HttpUtil.doPost;

public class BoardsConnectionIT {
    public static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_ADRESS_PATH = "/claverhause";
    private static final Logger logger = Logger.getLogger(BoardsConnectionIT.class);

    @Before
    public void setUp() {
    }

    @Test
    public void successBoardWorkTest() {
        URL reqURL = getRequestURL("/boards/data");

        BoardReq request = new BoardReq<>("username",
                "password",
                "123456",
                Arrays.asList(
                        new DeviceInfo.Builder().setId(1).setAck(0.7).setAdj(false).setCtrlVal(0.0).setRadioErr(false).build()
                ));
        HttpResponse response = getHttpResponse(reqURL, request);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void getBoardUIDTest() {
        URL reqURL = getRequestURL("/boards/board/uid/username");
        HttpResponse response = getHttpResponse(reqURL, null);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void successBoardRegistrationTest() {
        URL reqURL = getRequestURL("/boards/board/registration");

        BoardReq request = new BoardReq<>("username",
                "password",
                "123456",
                Arrays.asList(
                        new DeviceSetting.Builder().setId(1).setAdj(true).setRotate(false).setSignaling(false).build()
                ));
        HttpResponse response = getHttpResponse(reqURL, request);
        Assert.assertEquals(200, response.getCode());
    }

    private HttpResponse getHttpResponse(URL reqURL, BoardReq body) {
        RequestBody jsonBody = null;
        try {
            if (body != null) {
                jsonBody = RequestBody.create(JSON_CONTENT_TYPE, JsonUtil.toJson(body));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Request req = new Request.Builder()
                .url(reqURL)
                .post(jsonBody)
                .build();
        return doPost(req);
    }

    private URL getRequestURL(String path) {
        try {
            return new URL("http", "localhost", 8090, BASE_ADRESS_PATH + path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
