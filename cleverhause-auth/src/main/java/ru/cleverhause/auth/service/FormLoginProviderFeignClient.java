package ru.cleverhause.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="${feign.clients.formLoginProvider.name}", url="${feign.clients.formLoginProvider.url}")
public interface FormLoginProviderFeignClient {

    @GetMapping(path = "${feign.clients.formLoginProvider.path}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Authentication getUserByUsername(@RequestBody Authentication authentication);
}
