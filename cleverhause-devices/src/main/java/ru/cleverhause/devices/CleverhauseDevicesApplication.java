package ru.cleverhause.devices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CleverhauseDevicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleverhauseDevicesApplication.class, args);
    }

}
