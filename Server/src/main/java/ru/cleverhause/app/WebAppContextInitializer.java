package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.app.config.BoardConfig;

/**
 * @author Aleksandr Ivanov
 * @version v1-0 $Date 3/5/2017
 */
@Order(value = 2)
public class WebAppContextInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
//        rootAppContext.register(ApplicationContextConfig.class);
//        ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
//        servletContext.addListener(listener);

//        AnnotationConfigWebApplicationContext boardDispatcherContext = new AnnotationConfigWebApplicationContext();
//        boardDispatcherContext.setParent(rootAppContext);
//        boardDispatcherContext.register(BoardConfig.class);
//        FrameworkServlet boardDispatcherservlet = new DispatcherServlet(rootAppContext);
//        ServletRegistration.Dynamic boardServletRegistration = servletContext.addServlet("boardDispatcher", boardDispatcherservlet);
//        boardServletRegistration.setLoadOnStartup(1);
//        boardServletRegistration.addMapping("/boards/");

//        AnnotationConfigWebApplicationContext frontDispatcherContext = new AnnotationConfigWebApplicationContext();
//        frontDispatcherContext.setParent(rootAppContext);
//        frontDispatcherContext.register(FrontConfig.class);
//        FrameworkServlet frontDispatcherservlet = new DispatcherServlet(frontDispatcherContext);
//        ServletRegistration.Dynamic frontServletRegistration = servletContext.addServlet("frontDispatcher", frontDispatcherservlet);
//        frontServletRegistration.setLoadOnStartup(1);
//        frontServletRegistration.addMapping("/site/");
//    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/boards/*"};
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{BoardConfig.class};
    }

    @Override
    protected String getServletName() {
        return "boardDispatcher";
    }
}