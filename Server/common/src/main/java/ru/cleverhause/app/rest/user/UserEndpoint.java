package ru.cleverhause.app.rest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cleverhause.app.validator.UserValidator;
import ru.cleverhause.persist.entities.User;
import ru.cleverhause.service.security.SecurityService;
import ru.cleverhause.service.user.UserService;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 11/27/2017.
 */

@Controller
public class UserEndpoint {

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