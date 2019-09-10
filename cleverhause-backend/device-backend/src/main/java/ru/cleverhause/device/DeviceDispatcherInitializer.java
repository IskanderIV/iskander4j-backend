package ru.cleverhause.device;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.cleverhause.device.config.DeviceAppConfig;
import ru.cleverhause.device.config.mvc.DeviceWebMvcConfig;

public class DeviceDispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{DeviceAppConfig.class};
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DeviceWebMvcConfig.class};
    }

    @Override
    protected String getServletName() {
        return "deviceDispatcher";
    }
}