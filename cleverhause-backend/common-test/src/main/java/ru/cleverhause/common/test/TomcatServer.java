package ru.cleverhause.common.test;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.SpringServletContainerInitializer;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.io.IOException;

abstract public class TomcatServer {
    private Tomcat server = new Tomcat();
    private Context context;

    protected TomcatServer() throws IOException {
        String baseDir = System.getProperty("java.io.tmpdir");
        File docBase = new File(baseDir);
        server.setPort(getPort());
        server.setHostname(getHost());
        server.setBaseDir(baseDir);
        String appBase = ".";
        server.getHost().setAppBase(baseDir);//(appBase);
        server.getHost().setAutoDeploy(true);
        server.getHost().setDeployOnStartup(true);
//        server.setAddDefaultWebXmlToWebapp(false);

//        String contextPath = "/" + getApplicationId();
        File webApp = new File(baseDir);
//        File oldWebApp = new File(webApp.getAbsolutePath());
//        FileUtils.deleteDirectory(oldWebApp);

        context = server.addContext("", docBase.getAbsolutePath());
        ServletContainerInitializer springSci = new SpringServletContainerInitializer();
//        springSci.onStartup(Set.of(getServletContainerInitializer()), context.getServletContext());
        context.addServletContainerInitializer(springSci, null);
//        server.addWebapp(mTomcat.getHost(), contextPath, webApp.getAbsolutePath());
    }

    public void start() throws LifecycleException {
        server.start();
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
}
