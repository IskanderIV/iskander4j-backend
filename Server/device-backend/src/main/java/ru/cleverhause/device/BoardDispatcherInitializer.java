package ru.cleverhause.device;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.device.config.mvc.BoardConfig;

/**
 * @author Aleksandr Ivanov
 * @version v1-0 $Date 3/5/2017
 */
@Order(value = 3)
public class BoardDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

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