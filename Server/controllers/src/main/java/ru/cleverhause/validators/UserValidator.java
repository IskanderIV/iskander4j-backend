package ru.cleverhause.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.cleverhause.api.persist.entities.User;
import ru.cleverhause.api.service.user.UserService;

/**
 * Validator for {@link User} class,
 * implements {@link Validator} interface.
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 3/4/2018.
 */

@Component
public class UserValidator implements Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);

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
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Loginform.username.size");
        }

        if (userService.findByUserName(user.getUsername()) != null) {
            errors.rejectValue("username", "Loginform.username.duplicate");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Requared");
        if (user.getPassword().length() < 3 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Loginform.password.size");
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Loginform.password.duplicate");
        }
    }
}
