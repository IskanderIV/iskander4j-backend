package ru.cleverhause.device;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.device.config.ApplicationContextConfig;
import ru.cleverhause.device.config.mvc.DeviceWebMvcConfig;

/**
 * @author Aleksandr Ivanov
 */
public class BoardDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/*"};
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ApplicationContextConfig.class};
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DeviceWebMvcConfig.class};
    }

    @Override
    protected String getServletName() {
        return "boardDispatcher";
    }
}