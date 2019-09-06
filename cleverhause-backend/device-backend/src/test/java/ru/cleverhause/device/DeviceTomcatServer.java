package ru.cleverhause.device;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.SpringServletContainerInitializer;
import ru.cleverhause.common.test.TomcatServer;
import ru.cleverhause.device.BoardDispatcherInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DeviceTomcatServer extends TomcatServer {
    private Tomcat server = new Tomcat();

    protected DeviceTomcatServer() throws IOException {
        super();
        server.setPort(getPort());
        server.setHostname(getHost());
        String appBase = ".";
        server.getHost().setAppBase(appBase);

        File docBase = new File(System.getProperty("java.io.tmpdir"));
        Context context = server.addContext("", docBase.getAbsolutePath());

        ServletContainerInitializer springSci = new SpringServletContainerInitializer();
        ServletContext servletContext = new MockServletContext();
//        springSci.onStartup(Set.of(BoardDispatcherInitializer.class), servletContext);
        context.addServletContainerInitializer(springSci, null);
    }

    public void start() throws LifecycleException {
        server.start();
        server.getServer().await();
    }

    public void stop() throws LifecycleException {
        server.getServer().stop();
    }

    protected int getPort() {
        return 8080;
    }

    protected String getHost() {
        return "localhost";
    }

    protected Class<?> getServletContainerInitializer() {
        return BoardDispatcherInitializer.class;
    }
}
