package ru.cleverhause.devices.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;
import ru.cleverhause.devices.dto.request.DevicesControlRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DeviceControlRequestValidator implements ConstraintValidator<ValidDeviceControlRequest, DevicesControlRequest> {

    protected static final String NON_NULL_REQUEST_MSG = "Body should not be null";
    protected static final String NON_NULL_INSERT_DEVICE_ID_MSG = "Device id should be blank";

    @Override
    public boolean isValid(DevicesControlRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            changeErrorMessage(context, NON_NULL_REQUEST_MSG);
            return false;
        }
        if (request.getDevices().stream().anyMatch(device -> StringUtils.isBlank(device.getDeviceId()))) {
            changeErrorMessage(context, NON_NULL_INSERT_DEVICE_ID_MSG);
            return false;
        }
        return true;
    }

    private void changeErrorMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
