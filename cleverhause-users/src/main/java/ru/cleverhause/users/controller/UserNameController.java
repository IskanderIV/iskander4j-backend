package ru.cleverhause.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserNameController {

    @RequestMapping(value = "/username")
    public String username() {
        return "username";
    }
}
