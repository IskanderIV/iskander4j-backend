package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import ru.cleverhause.app.config.root.ApplicationContextConfig;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/11/2018.
 */
@Order(value = 1)
public class RootWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

//    @Override
//    protected WebApplicationContext createRootApplicationContext() {
//        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
//        rootAppContext.register(ApplicationContextConfig.class);
//
//        return rootAppContext;
//    }

    public RootWebAppInitializer() {
        super(ApplicationContextConfig.class);
    }
}
