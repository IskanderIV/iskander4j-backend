package ru.cleverhause.devices.exception;

import lombok.Getter;

@Getter
public class DeviceServiceException extends RuntimeException {
    private DeviceServiceErrorCode code;

    public DeviceServiceException(DeviceServiceErrorCode code) {
        this.code = code;
    }

    public DeviceServiceException(DeviceServiceErrorCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public DeviceServiceException(DeviceServiceErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
