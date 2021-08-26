package ru.cleverhause.devices.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = {DeviceControlRequestValidator.class})
public @interface ValidDeviceControlRequest {

    String message() default "Some...";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
