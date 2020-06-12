package ru.cleverhause.users.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = UserInfoValidator.class)
public @interface ValidUserInfo {
}
