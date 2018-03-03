package ru.cleverhause.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.cleverhause.model.User;
import ru.cleverhause.service.UserService;

/**
 * Validator for {@link ru.cleverhause.model.User} class,
 * implements {@link Validator} interface.
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 3/4/2018.
 */

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Requared");
        if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Loginform.username.size");
        }

        if (userService.findByUserName(user.getUsername()) != null) {
            errors.rejectValue("username", "Loginform.username.duplicate");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Requared");
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Loginform.password.size");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Loginform.password.duplicate");
        }
    }
}
