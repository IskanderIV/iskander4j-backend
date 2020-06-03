package ru.cleverhause.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties
public class CleverhauseAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleverhauseAuthApplication.class, args);
	}

}
