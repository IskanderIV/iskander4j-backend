package ru.cleverhause.provider.formlogin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

//@RequiredArgsConstructor
//@RestController("/login")
public class FormLoginProviderController {

//    private final UserDetailsService userDetailsService;
//
//    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
//    public ResponseEntity<Authentication> authenticate(@RequestBody UsernamePasswordAuthenticationToken body) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(body.getPrincipal().toString());
//        userDetails.
//        return ResponseEntity.ok();
//    }
}
