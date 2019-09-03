package ru.cleverhause.device.filters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import javax.servlet.Filter;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/2/2018.
 */
public class SecurityFilterChainPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("springSecurityFilterChain")) {
            FilterChainProxy fc = (FilterChainProxy) bean;
            List<Filter> filters = fc.getFilters("/boards/**");

            Filter removed = null;
            for (Filter filter : filters) {
                if (filter.getClass().isAssignableFrom(SecurityContextHolderAwareRequestFilter.class)) {
                    removed = filter;
                }
            }

            filters.remove(removed);
        }

        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
