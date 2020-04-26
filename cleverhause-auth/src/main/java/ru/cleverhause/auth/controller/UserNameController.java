package ru.cleverhause.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;

@Slf4j
@RestController
public class UserNameController {

    @RequestMapping(value = "/oauth/test")
    public ResponseEntity<?> username(@RequestParam(required = true) String name) {
        log.info("Username= {}", name);
        return new ResponseEntity<>(Collections.singletonMap("name", name), HttpStatus.OK);
    }
}
