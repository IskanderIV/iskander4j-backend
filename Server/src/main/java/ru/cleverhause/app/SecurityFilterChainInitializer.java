package ru.cleverhause.app;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
@Order(value = 1)
public class SecurityFilterChainInitializer extends AbstractSecurityWebApplicationInitializer {
}
