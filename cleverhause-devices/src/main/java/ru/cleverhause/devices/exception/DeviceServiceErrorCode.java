package ru.cleverhause.devices.exception;

import lombok.Getter;

@Getter
public enum DeviceServiceErrorCode {
    INSERTED_DEVICE_ALREADY_EXIST("001", "Inserted device is already exist");

    private final String code;
    private final String msg;

    DeviceServiceErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
