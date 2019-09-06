package ru.cleverhause.device.config.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {
        "ru.cleverhause.device.services"})
public class ServiceConfig {
}
