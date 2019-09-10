package ru.cleverhause.web;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.web.config.WebBackAppConfig;
import ru.cleverhause.web.config.mvc.WebMvcConfig;

public class FrontDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                WebBackAppConfig.class
        };
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }

    @Override
    protected String getServletName() {
        return "frontDispatcher";
    }
}