package ru.cleverhause.devices.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DeviceParamsRequestValidator implements ConstraintValidator<ValidDeviceParamsRequest, DeviceParamsRequest> {

    protected static final String NON_NULL_REQUEST_MSG = "Body should not be null";
    protected static final String NON_NULL_INSERT_DEVICE_ID_MSG = "Device id should be null if request is of insert type";
    protected static final String NULL_UPDATE_DEVICE_ID_MSG = "Device id should not be null if request is of update type";
    protected static final String NON_BLANK_DEVICE_NAME_USERNAME_MSG = "Device name and username should not be blank";

    private ValidDeviceParamsRequest.RequestType type = ValidDeviceParamsRequest.RequestType.INSERT;

    @Override
    public void initialize(ValidDeviceParamsRequest constraintAnnotation) {
        this.type = constraintAnnotation.type();
        log.info("DeviceParamsRequestValidator {}, type {} ", this, type);
    }

    @Override
    public boolean isValid(DeviceParamsRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            changeErrorMessage(context, NON_NULL_REQUEST_MSG);
            return false;
        }
        if (type == ValidDeviceParamsRequest.RequestType.INSERT) {
            if (ObjectUtils.isNotEmpty(request.getDeviceId())) {
                changeErrorMessage(context, NON_NULL_INSERT_DEVICE_ID_MSG);
                return false;
            }
        } else {
            if (ObjectUtils.isEmpty(request.getDeviceId())) {
                changeErrorMessage(context, NULL_UPDATE_DEVICE_ID_MSG);
                return false;
            }
        }
        if (StringUtils.isAnyBlank(request.getDeviceName(), request.getUsername())) {
            changeErrorMessage(context, NON_BLANK_DEVICE_NAME_USERNAME_MSG);
            return false;
        }
        return true;
    }

    private void changeErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
