package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.app.config.front.FrontConfig;

/**
 * @author Aleksandr Ivanov
 * @version v1-0 $Date 3/5/2017
 */
@Order(value = 4)
public class FrontDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/site/*"};
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{FrontConfig.class};
    }

    @Override
    protected String getServletName() {
        return "frontDispatcher";
    }
}