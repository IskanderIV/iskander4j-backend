package ru.cleverhause.webapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
public class WebApiController {

    @GetMapping(value = {"/dummy"})
    public ResponseEntity<?> dummy(@CookieValue(AUTHORIZATION) String authorizationHeader) {
        log.debug("Authorization Header: {}", authorizationHeader);
        return ResponseEntity.ok().build();
    }
}
