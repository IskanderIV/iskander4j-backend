package ru.cleverhause.web.api.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.common.api.service.security.SecurityService;
import ru.cleverhause.common.persist.api.entity.User;
import ru.cleverhause.web.api.service.UserService;
import ru.cleverhause.web.api.validation.UserValidator;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 11/27/2017.
 */

@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

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
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout) {
        if (error != null) {
            model.addAttribute("error", "User or password is incorrect.");
        }
        if (logout != null) {
            model.addAttribute("logout", true);
            return "home";
        }

        return "login";
    }

    @PostMapping(value = {"/login"})
    public String login(User userForm, BindingResult bindingResult, Model model) {
//        userValidator.validate(userForm, bindingResult);
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }

        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        return "redirect:/myboard/myboard";
    }

    @GetMapping(value = {"/admin"})
    public String admin() {

        return "admin";
    }
}