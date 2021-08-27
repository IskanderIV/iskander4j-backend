package ru.cleverhause.users.authorities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    DEVICE_READ("device:read"),
    DEVICE_UPDATE("device:update");

    private final String permission;
}
