package ru.cleverhause.common.test;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

abstract public class TomcatServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatServer.class);

    private Tomcat server = new Tomcat();
    private Context context;

    protected TomcatServer() throws IOException {
        String baseDir = System.getProperty("java.io.tmpdir");
        server.getServer().setPort(getPort());
        server.setHostname(getHost());
        server.setBaseDir(baseDir);
        String appBase = ".";
        server.getHost().setAppBase(baseDir);
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(getPort());
        server.setConnector(connector);
//        server.getHost().setAutoDeploy(true);
//        server.getHost().setDeployOnStartup(true);
//        server.setAddDefaultWebXmlToWebapp(false);
        server.addWebapp(getContextPath(), new File(getWarPath()).getAbsolutePath());
    }

    public void start() throws LifecycleException {
        server.start();
        LOGGER.info("TOMCAT SERVER has been started!");
//        server.getServer().await();
    }

    public void stop() throws LifecycleException {
        server.getServer().stop();
    }

    protected int getPort() {
        return 8090;
    }

    protected int getTomcatPort() {
        return server.getConnector().getLocalPort();
    }

    public Tomcat getServer() {
        return server;
    }

    protected String getHost() {
        return "localhost";
    }

    public Context getContext() {
        return context;
    }

    abstract protected Class<?> getServletContainerInitializer();

    abstract protected String getContextPath();

    abstract protected String getWarPath();
}
