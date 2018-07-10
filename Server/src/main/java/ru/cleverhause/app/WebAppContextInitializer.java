package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import ru.cleverhause.app.config.ApplicationContextConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author Aleksandr Ivanov
 * @version v1-0 $Date 3/5/2017
 */
@Order(value = 1)
public class WebAppContextInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        rootAppContext.register(ApplicationContextConfig.class);
        ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
        servletContext.addListener(listener);

//        AnnotationConfigWebApplicationContext boardDispatcherContext = new AnnotationConfigWebApplicationContext();
//        boardDispatcherContext.setParent(rootAppContext);
//        boardDispatcherContext.register(BoardConfig.class);
        FrameworkServlet boardDispatcherservlet = new DispatcherServlet(rootAppContext);
        ServletRegistration.Dynamic boardServletRegistration = servletContext.addServlet("boardDispatcher", boardDispatcherservlet);
        boardServletRegistration.setLoadOnStartup(1);
        boardServletRegistration.addMapping("/boards/");

//        AnnotationConfigWebApplicationContext frontDispatcherContext = new AnnotationConfigWebApplicationContext();
//        frontDispatcherContext.setParent(rootAppContext);
//        frontDispatcherContext.register(FrontConfig.class);
//        FrameworkServlet frontDispatcherservlet = new DispatcherServlet(frontDispatcherContext);
//        ServletRegistration.Dynamic frontServletRegistration = servletContext.addServlet("frontDispatcher", frontDispatcherservlet);
//        frontServletRegistration.setLoadOnStartup(1);
//        frontServletRegistration.addMapping("/site/");
    }
}