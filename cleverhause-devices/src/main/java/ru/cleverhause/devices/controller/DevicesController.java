package ru.cleverhause.devices.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.devices.dto.request.DeviceDataRequest;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;
import ru.cleverhause.devices.dto.request.DevicesControlRequest;
import ru.cleverhause.devices.dto.response.DeviceControlResponse;
import ru.cleverhause.devices.dto.response.DevicesDataResponse;
import ru.cleverhause.devices.dto.response.UserDevicesResponse;
import ru.cleverhause.devices.service.DeviceService;
import ru.cleverhause.devices.validation.ValidDeviceControlRequest;
import ru.cleverhause.devices.validation.ValidDeviceParamsRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api")
public class DevicesController {

    private final DeviceService deviceService;

    /**
     * Interaction with web
     * Should be cacheable and cache must be evicted if params changed
     */
    @GetMapping("/devices/params")
    public ResponseEntity<UserDevicesResponse> getDevicesParams(@NotBlank @RequestParam("username") String username) {
        log.info("Input request getDevicesParams for username: {}", username);
        UserDevicesResponse userDevicesResponse = deviceService.findAllByUsername(username);
        log.info("User '{}' devices: {}", username, userDevicesResponse);
        return ResponseEntity.ok(userDevicesResponse);
    }

    /**
     * Interaction with web
     */
    @GetMapping("/devices/data")
    public ResponseEntity<DevicesDataResponse> getDevicesData(@NotBlank @RequestParam("ids") String deviceIds) {
        log.info("Input request getDevicesData for ids: {}", deviceIds);
        DevicesDataResponse devicesDataResponse = deviceService.findAllByIds(idsToList(deviceIds));
        log.info("Devices data: {}", devicesDataResponse);
        return ResponseEntity.ok(devicesDataResponse);
    }

    /**
     * Interaction with web
     */
    @PutMapping("/devices/control")
    public ResponseEntity<?> updateDevicesControl(@ValidDeviceControlRequest @RequestBody DevicesControlRequest devicesControlRequest) {
        log.info("Input request updateDevicesControl with deviceIds: {}", devicesControlRequest);
        deviceService.updateControl(devicesControlRequest);
        log.info("New controls for devices were accepted");
        return ResponseEntity.accepted().build();
    }

    /**
     * Interaction with web
     */
    @DeleteMapping("/devices")
    public ResponseEntity<?> deleteDevices(@NotBlank @RequestParam("ids") String deviceIds) {
        log.info("Input request deleteDevices for ids: {}", deviceIds);
        deviceService.deleteDevices(idsToList(deviceIds));
        log.info("Deletion of devices were accepted");
        return ResponseEntity.accepted().build();
    }

    /**
     * Interaction with device
     */
    @PostMapping("/device/params")
    public ResponseEntity<?> insertDeviceParams(@ValidDeviceParamsRequest @RequestBody DeviceParamsRequest deviceParamsRequest) {
        log.info("Input request insertDeviceParams: {}", deviceParamsRequest);
        String deviceId = deviceService.insertDeviceParams(deviceParamsRequest);
        log.info("Device '{}' was successfully registered", deviceId);
        return ResponseEntity.ok(Map.of("deviceId", deviceId));
    }

    /**
     * Interaction with device
     */
    @PutMapping("/device/params")
    public ResponseEntity<?> updateDeviceParams(@ValidDeviceParamsRequest(type = ValidDeviceParamsRequest.RequestType.UPDATE)
                                                @RequestBody DeviceParamsRequest deviceParamsRequest) {
        log.info("Input request updateDeviceParams: {}", deviceParamsRequest);
        deviceService.updateDeviceParams(deviceParamsRequest);
        log.info("Device '{}'params were updated", deviceParamsRequest.getDeviceId());
        return ResponseEntity.accepted().build();
    }

    /**
     * Interaction with device
     */
    @PutMapping("/device/data")
    public ResponseEntity<?> updateDeviceData(@Valid @RequestBody DeviceDataRequest deviceDataRequest) {
        log.info("Input request updateDeviceData with body: {}", deviceDataRequest);
        DeviceControlResponse deviceControlResponse = deviceService.updateDeviceData(deviceDataRequest);
        log.info("Device control response: {}", deviceControlResponse);
        return ResponseEntity.ok(deviceControlResponse);
    }

    private List<String> idsToList(String ids) {
        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}