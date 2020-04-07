package ru.cleverhause.cleverhausezuulapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class CleverhauseZuulApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleverhauseZuulApiGatewayApplication.class, args);
    }

}
