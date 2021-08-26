package ru.cleverhause.devices.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class DeviceExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {DeviceServiceException.class})
    public ResponseEntity<?> handleDeviceExceptions(DeviceServiceException dse) {
        log.error(dse.getLocalizedMessage(), dse);
        return ResponseEntity.of(Optional.of(Map.of("code", dse.getCode().getCode(), "msg", dse.getCode().getMsg())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleOthers(Exception e) {
        log.error(e.getLocalizedMessage(), e);
        return ResponseEntity.of(Optional.of(Map.of("code", "000", "msg", e.getLocalizedMessage())));
    }
}
