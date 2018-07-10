package ru.cleverhause.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/9/2018.
 */
//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = {"ru.cleverhause.app.rest.site"})
//@PropertySource(value = {"classpath:application.properties"})
//@Import(value = {FrontWebSecurityConfig.class})
public class FrontConfig {

//    @Bean
//    public static PropertyPlaceholderConfigurer placeHolderConfigurer() {
//        return new PropertyPlaceholderConfigurer();
//    }

    //This is a straightforward mechanism to map views names to URLs with no need for an explicit controller in between
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        super.addViewControllers(registry);
//
//        registry.addViewController("/somepage.html");
//    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/"); //
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
