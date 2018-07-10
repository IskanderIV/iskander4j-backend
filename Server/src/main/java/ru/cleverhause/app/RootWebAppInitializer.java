package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import ru.cleverhause.app.config.ApplicationContextConfig;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/11/2018.
 */
@Order(value = 1)
public class RootWebAppInitializer extends AbstractContextLoaderInitializer {
    @Nullable
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
        rootAppContext.register(ApplicationContextConfig.class);

        return rootAppContext;
    }
}
