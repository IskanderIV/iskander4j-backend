package ru.cleverhause.util;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cleverhause.rest.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static HttpResponse doPost(final Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        final Response response;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return convertToCleverhauseResponse(response);
    }

    public static HttpResponse doPost(final HttpUrl url, final RequestBody body, final Headers headers) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
//        RequestBody jsonBody = RequestBody.create(JSON_CONTENT_TYPE, bodyInString);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        final Response response = httpClient.newCall(request).execute();

        return convertToCleverhauseResponse(response);
    }

//    private static CloseableHttpClient createDefaultWithCookie() {
//        BasicCookieStore cookieStore = new BasicCookieStore();
//        BasicClientCookie cookie = new BasicClientCookie(DEFAULT_SESSION_COOKIE_NAME, "mockSessionCookie");
//        cookie.setDomain("localhost");
//        cookie.setPath("/");
//        cookieStore.addCookie(cookie);
//        return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
//    }

    private static HttpResponse convertToCleverhauseResponse(final Response response) {
        // obtain headers
        // get Headers from standard response for putting it in My Custom response
        final Map<String, List<String>> respHeader = response.headers().toMultimap();
        // obtain body
        final ResponseBody responseBody = response.body();
        final String body;
        try {
            body = responseBody != null ? responseBody.string() : null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        // obtain code
        final int statusCode = response.code();
        LOGGER.info("Response: {}, {}", statusCode, body);

        return new HttpResponse(statusCode, body, respHeader);
    }
}
