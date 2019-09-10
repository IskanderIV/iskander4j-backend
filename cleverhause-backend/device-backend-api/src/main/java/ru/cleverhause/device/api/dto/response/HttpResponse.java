package ru.cleverhause.device.api.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/4/2018.
 */
public class HttpResponse {
    private String body;
    private Map<String, List<String>> headers;
    private int code;

    public HttpResponse(int code, String body, Map<String, List<String>> headers) {
        this.body = body;
        this.headers = headers;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getBody() {
        return this.body;
    }
//
//    public Map<String, Object> getBodyAsJson() {
//        return JsonUtil.jsonToMap(this.body);
//    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public String cookies(String... expectedCookies) {
        List<String> res = new ArrayList<>();
        List<String> cookies = this.headers.get("Set-Cookie");
        if (cookies == null) {
            return null;
        } else {
            for (String cookie : cookies) {
                for (String expectedCookie : expectedCookies) {
                    if (cookie.contains(expectedCookie)) {
                        res.add(cookie.split(Pattern.quote(";"))[0]);
                    }
                }
            }

            return StringUtils.join(res, ";");
        }
    }

    public String getCookie(String name) {
        List<String> cookies = this.headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.contains(name)) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public String getHeader(String name) {
        List<String> header = this.headers.get(name);

        return header == null ? null : header.get(0);
    }


    public <T> T parseJson(Class<T> type, ObjectMapper mapper) throws IOException {
        return mapper.readValue(this.body, type);
    }
}