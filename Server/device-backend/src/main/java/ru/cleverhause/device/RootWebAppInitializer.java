package ru.cleverhause.device;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import ru.cleverhause.device.config.root.ApplicationContextConfig;

import javax.servlet.ServletContext;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/11/2018.
 */
@Order(value = 1)
public class RootWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    public RootWebAppInitializer() {
        super(ApplicationContextConfig.class);
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        servletContext.setInitParameter("spring.profiles.active", "prod");
        super.beforeSpringSecurityFilterChain(servletContext);
    }
}