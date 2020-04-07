package ru.cleverhause.eurekadiscoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CleverhauseEurekaDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleverhauseEurekaDiscoveryServiceApplication.class, args);
	}

}
