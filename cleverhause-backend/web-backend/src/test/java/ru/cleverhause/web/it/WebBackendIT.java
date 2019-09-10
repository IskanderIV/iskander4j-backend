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
import org.apache.http.message.BasicHeader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import ru.cleverhause.common.test.TomcatHelper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebBackendIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebBackendIT.class);

    private TomcatHelper tomcatHelper;

    @Before
    public void setUp() throws LifecycleException, IOException {
        tomcatHelper = new TomcatHelper() {
            @Override
            protected String getContextPath() {
                return "/user-backend";
            }

            @Override
            protected String getWarPath() {
                return "target/user-backend.war";
            }
        };

        tomcatHelper.start();
    }

    @After
    public void teardown() throws Throwable {
        if (tomcatHelper.getServer() != null
                && tomcatHelper.getServer().getState() != LifecycleState.DESTROYED) {
            if (tomcatHelper.getServer().getState() != LifecycleState.STOPPED) {
                tomcatHelper.stop();
            }
            tomcatHelper.getServer().destroy();
        }
    }

    @Test
    public void healthCheckWebBackendIT() throws InterruptedException {
        while(!tomcatHelper.isTomcatStarted()) {
            Thread.sleep(100L);
        }
        HttpClient client = HttpClientBuilder.create().disableContentCompression().build();
        try {
            final String endpoint = "/user-backend/home";
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(tomcatHelper.getHostName())
                    .setPort(tomcatHelper.getPort())
                    .setPath(endpoint)
                    .build();
            final HttpGet httpGet = new HttpGet(uri);
            final Header[] headers = getHttpHeaders(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpGet.setHeaders(headers);
            HttpResponse response = client.execute(httpGet);
            Optional.ofNullable(response)
                    .map(HttpResponse::getStatusLine)
                    .ifPresent(status -> {
                        Assert.assertEquals(200, status.getStatusCode());
                    });
        } catch (IOException | URISyntaxException e) {
            LOGGER.info("LOST CONNECTION");
            e.printStackTrace();
        }
    }

    private Header[] getHttpHeaders(String contentType) {
        final List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, contentType));
        return headers.toArray(new Header[0]);
    }
}
