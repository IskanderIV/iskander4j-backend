package ru.cleverhause.device.config.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/2/2018.
 */
@Configuration
@ComponentScan(value = {
        "ru.cleverhause.service.board",
        "ru.cleverhause.service.user",
        "ru.cleverhause.service.site",
        "ru.cleverhause.filters.mapper"})
public class ServiceConfig {
}
