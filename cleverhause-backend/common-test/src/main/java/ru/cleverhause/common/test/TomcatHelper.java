package ru.cleverhause.common.test;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SocketUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

abstract public class TomcatHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatHelper.class);
    private static final String DEFAULT_HOST_NAME = "localhost";
    private static final int DEFAULT_PORT = 8090;
    private static final String DEFAULT_APP_BASE = ".";

    private Tomcat tomcat;
    private String hostName;
    private Integer port;
    private String baseDir; // where tomcat lives
    private String appBase; // analog of folder name inside webapp inside real tomcat ("." = ROOT, "/some"=/some)
    private AtomicBoolean isTomcatStarted = new AtomicBoolean(false);

    public TomcatHelper() {
        this(DEFAULT_HOST_NAME);
    }

    public TomcatHelper(String hostName) {
        this(hostName, SocketUtils.findAvailableTcpPort(), DEFAULT_APP_BASE);
    }

    public TomcatHelper(String hostName, int port) {
        this(hostName, port, DEFAULT_APP_BASE);
    }

    public TomcatHelper(String hostName, int port, String appBase) {
        this.hostName = hostName;
        this.port = port;
        this.appBase = appBase;
        setBaseDir();
    }

    private void setBaseDir() {
        baseDir = System.getProperty("java.io.tmpdir") + "/__tomcat/baseDir";
        File baseDirFile = new File(baseDir);
        if (baseDirFile.exists() && baseDirFile.isDirectory()) {
            try {
                FileUtils.deleteDirectory(baseDirFile);
            } catch (IOException e) {
                LOGGER.error("TOMCAT SERVER starting has been aborted!");
                throw new RuntimeException("Can't delete base directory with previous project", e);
            }
        }
    }

    public void start() {
        CompletableFuture.runAsync(this::startTomcat);
    }

    public void stop() throws LifecycleException {
        tomcat.getServer().stop();
    }

    public Boolean isTomcatStarted() {
        try {
            // wait some time after tomcat has been started
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            LOGGER.error("TOMCAT SERVER starting has been aborted!");
            throw new RuntimeException("TOMCAT SERVER starting has been aborted!", e);
        }
        return isTomcatStarted.get();
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getAppBase() {
        return appBase;
    }

    public Server getServer() {
        return tomcat.getServer();
    }

    abstract protected String getContextPath();

    abstract protected String getWarPath();

    private void startTomcat() {
        try {
            tomcat = new Tomcat();
            tomcat.setPort(getPort());
            tomcat.setHostname(getHostName());
            tomcat.setBaseDir(getBaseDir());
            tomcat.getHost().setAppBase(getAppBase());
            tomcat.getConnector();
            tomcat.addWebapp(getContextPath(), new File(getWarPath()).getAbsolutePath());
            tomcat.start();
            LOGGER.info("TOMCAT SERVER has been started!");
            isTomcatStarted.getAndSet(true);
        } catch (LifecycleException e) {
            LOGGER.error("TOMCAT SERVER starting has been aborted!");
            throw new RuntimeException("TOMCAT SERVER starting has been aborted!", e);
        }
    }
}
