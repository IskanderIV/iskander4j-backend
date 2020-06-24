package ru.cleverhause.devices.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = {DeviceParamsRequestValidator.class})
public @interface ValidDeviceParamsRequest {

    RequestType type() default RequestType.INSERT;

    String message() default "Some...";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum RequestType {
        INSERT, UPDATE
    }
}
