package ru.cleverhause.it.arduino;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import ru.cleverhause.rest.dto.DeviceData;
import ru.cleverhause.rest.dto.DeviceSetting;
import ru.cleverhause.rest.dto.request.BoardRequestBody;
import ru.cleverhause.rest.dto.response.HttpResponse;
import ru.cleverhause.util.JsonUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static ru.cleverhause.util.HttpUtil.execute;

public class BoardsConnectionIT {
    public static final MediaType JSON_CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_ADRESS_PATH = "/cleverhause";
    private static final Logger logger = Logger.getLogger(BoardsConnectionIT.class);

    @Before
    public void setUp() {
    }

    @Test
    public void successBoardWorkTest() {
        URL reqURL = getRequestURL("/boards/board/data");

        BoardRequestBody request = new BoardRequestBody<>("username",
                "password",
                123456L,
                Arrays.asList(
                        new DeviceData.Builder().setId(1).setAck(0.7).setAdj(false).setCtrlVal(0.0).setRadioErr(false).build()
                ));
        HttpResponse response = getHttpResponse(HttpMethod.POST, reqURL, request);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void getBoardUIDTest() {
        URL reqURL = getRequestURL("/boards/board/uid");
        HttpResponse response = getHttpResponse(HttpMethod.GET, reqURL, null);
        Assert.assertEquals(200, response.getCode());
    }

    @Test
    public void successBoardRegistrationTest() {
        URL reqURL = getRequestURL("/boards/board/registration");

        BoardRequestBody request = new BoardRequestBody<>("username",
                "password",
                123456L,
                Arrays.asList(
                        new DeviceSetting.Builder().setId(1).setAdj(true).setRotate(false).setSignaling(false).build()
                ));
        HttpResponse response = getHttpResponse(HttpMethod.POST, reqURL, request);
        Assert.assertEquals(200, response.getCode());
    }

    private HttpResponse getHttpResponse(HttpMethod method, URL reqURL, BoardRequestBody body) {
        RequestBody jsonBody = null;
        try {
            if (body != null) {
                jsonBody = RequestBody.create(JSON_CONTENT_TYPE, JsonUtil.toJson(body));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(reqURL);
        switch (method) {
            case POST:
                reqBuilder.post(jsonBody);
                break;
            case GET:
            default:
                reqBuilder.get().addHeader("Content-type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        }
        return execute(reqBuilder.build());
    }

    private URL getRequestURL(String path) {
        try {
            return new URL("http", "localhost", 8090, BASE_ADRESS_PATH + path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
