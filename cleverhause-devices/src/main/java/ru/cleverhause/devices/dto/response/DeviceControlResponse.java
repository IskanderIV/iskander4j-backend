package ru.cleverhause.devices.dto.response;

import lombok.Builder;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceControlDto;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;

import java.util.List;

@ToString(callSuper = true)
public class DeviceControlResponse extends DeviceControlDto {

    @Builder(builderMethodName = "deviceControlResponseBuilder")
    public DeviceControlResponse(String deviceId, List<SensorControlDto> sensors) {
        super(deviceId, sensors);
    }
}
