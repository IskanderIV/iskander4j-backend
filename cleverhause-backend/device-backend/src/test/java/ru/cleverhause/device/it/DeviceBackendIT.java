package ru.cleverhause.device.it;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import ru.cleverhause.common.test.TomcatServer;
import ru.cleverhause.device.BoardDispatcherInitializer;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.device.dto.request.BoardRequestBody;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@RunWith(SpringJUnit4ClassRunner.class)
public class DeviceBackendIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceBackendIT.class);
    private static String HOST_NAME = "localhost";
    private static int PORT = 8090;

    private TomcatServer tomcatServer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws LifecycleException, IOException {
        tomcatServer = new TomcatServer() {
            @Override
            protected Class<?> getServletContainerInitializer() {
                return BoardDispatcherInitializer.class;
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
    public void registerDeviceIT() {
        HttpClient client = HttpClients.createDefault();
        try {
            final String endpoint = "/device-backend";
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(HOST_NAME)
                    .setPort(PORT)
                    .setPath(endpoint)
                    .build();
            final HttpPost httpPost = new HttpPost(uri);
            final Header[] headers = getHttpHeaders(MediaType.APPLICATION_JSON_UTF8_VALUE);
            List<DeviceStructure> devices = List.of((DeviceStructure) getDevice(DeviceStructure.class));
            final HttpEntity entity = new StringEntity(getRequestBody(devices), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeaders(headers);

            HttpResponse response = client.execute(httpPost);
            Optional.ofNullable(response)
                    .map(HttpResponse::getStatusLine)
                    .ifPresent(status -> {
                        Assert.assertEquals(status.getStatusCode(), 200);
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
