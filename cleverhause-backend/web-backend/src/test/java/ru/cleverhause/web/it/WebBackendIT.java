package ru.cleverhause.web.it;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import ru.cleverhause.common.test.TomcatServer;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.device.dto.request.BoardRequestBody;
import ru.cleverhause.web.FrontDispatcherInitializer;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebBackendIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebBackendIT.class);
    private static String HOST_NAME = "localhost";
    private static int PORT = 8090;

    private TomcatServer tomcatServer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws LifecycleException, IOException {
        tomcatServer = new TomcatServer() {
            @Override
            protected Class<?> getServletContainerInitializer() {
                return FrontDispatcherInitializer.class;
            }

            @Override
            protected String getContextPath() {
                return "/user-backend";
            }

            @Override
            protected String getWarPath() {
                return "target/user-backend.war";
            }

            @Override
            protected String getHost() {
                return HOST_NAME;
            }

            @Override
            protected int getPort() {
                return PORT;
            }
        };

        tomcatServer.start();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @After
    public final void teardown() throws Throwable {
        if (tomcatServer.getServer() != null
                && tomcatServer.getServer().getServer().getState() != LifecycleState.DESTROYED) {
            if (tomcatServer.getServer().getServer().getState() != LifecycleState.STOPPED) {
                tomcatServer.stop();
            }
            tomcatServer.getServer().destroy();
        }
    }

    @Test
    public void healthCheckWebBackendIT() throws InterruptedException {
        HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
        try {
            final String endpoint = "/user-backend/home";
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(HOST_NAME)
                    .setPort(PORT)
                    .setPath(endpoint)
                    .build();
            LOGGER.info("URL:{}", uri);
            final HttpGet httpGet = new HttpGet(uri);
            final Header[] headers = getHttpHeaders(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpGet.setHeaders(headers);
            LOGGER.info("Headers:{}", headers);
            LOGGER.info("Request:{}", httpGet);
            HttpResponse response = client.execute(httpGet);
            Optional.ofNullable(response)
                    .map(HttpResponse::getStatusLine)
                    .ifPresent(status -> {
                        System.out.println("EVERYTHING IS OK!!!!");
                        Assert.assertEquals(200, status.getStatusCode());
                    });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private <T extends Serializable> String getRequestBody(List<T> devices) {
        BoardRequestBody<T> body =
                new BoardRequestBody<>("", "", 1010101L, devices, null);
        return writeValue(body);
    }

    private Object getDevice(Class<?> type) {
        if (type.isAssignableFrom(DeviceStructure.class)) {
            return DeviceStructure.Builder.create().build();
        } else {
            return null;
        }
    }

    private Header[] getHttpHeaders(String contentType) {
        final List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, contentType));
//        headers.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, MediaType.));

        return headers.toArray(new Header[0]);
    }

    private <T extends Serializable> String writeValue(final T request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (IOException e) {
            LOGGER.error("Can't map to string request body {}", request);
            throw new RuntimeException("Can't write object to json");
        }
    }
}
