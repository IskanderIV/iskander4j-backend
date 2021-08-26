package ru.cleverhause.webapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/web")
@RequiredArgsConstructor
public class WebApiController {

    @DeleteMapping(value = {"/user"})
    public ResponseEntity<?> deleteUser(@RequestParam String userId) {
        log.debug("Delete user with his whole devices");
        return ResponseEntity.ok().build();
    }
}
