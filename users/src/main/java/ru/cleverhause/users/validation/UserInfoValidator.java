package ru.cleverhause.users.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import ru.cleverhause.users.dto.request.UserInfoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserInfoValidator implements ConstraintValidator<ValidUserInfo, UserInfoRequest> {
    private static final String USER_INFO_VALIDATION_MSG = "User info request must contain userId OR username";

    @Override
    public boolean isValid(UserInfoRequest userInfoRequest, ConstraintValidatorContext context) {
        boolean isValid = ObjectUtils.isNotEmpty(userInfoRequest.getUserId())
                || StringUtils.isNotBlank(userInfoRequest.getUserName());
        if (!isValid) {
            changeErrorMessage(context, USER_INFO_VALIDATION_MSG);
        }
        return isValid;
    }

    private void changeErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
