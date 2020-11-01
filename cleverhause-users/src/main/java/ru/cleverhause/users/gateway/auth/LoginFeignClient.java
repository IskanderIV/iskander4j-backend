package ru.cleverhause.users.gateway.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${feign.clients.auth-server.name}",
        url = "${feign.clients.auth-server.url}",
        configuration = LoginFeignClientConfiguration.class)
public interface LoginFeignClient {

    String GRANT_TYPE = "client_credentials";

    @PostMapping(path = "${feign.clients.auth-server.path}?grant_type=" + GRANT_TYPE)
    Object getToken();
}
