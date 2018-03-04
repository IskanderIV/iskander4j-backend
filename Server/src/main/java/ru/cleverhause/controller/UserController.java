package ru.cleverhause.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.cleverhause.model.User;
import ru.cleverhause.service.SecurityService;
import ru.cleverhause.service.UserService;
import ru.cleverhause.validator.UserValidator;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 11/27/2017.
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    //TODO replace to home controller
    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @GetMapping(value = {"/registration"})
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping(value = {"/registration"})
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());
        return "redirect:/home";
    }

    @GetMapping(value = {"/login"})
    public String login(Model model, String error, String loguot) {
        if (error != null) {
            model.addAttribute("error", "User or password is incorrect.");
        }
        if (error != null) {
            model.addAttribute("message", "Logged out successfully");
        }

        return "login";
    }

    @GetMapping(value = {"/admin"})
    public String admin(Model model) {
        return "admin";
    }
}