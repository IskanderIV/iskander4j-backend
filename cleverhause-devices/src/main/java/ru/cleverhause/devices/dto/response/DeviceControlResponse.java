package ru.cleverhause.devices.dto.response;

import lombok.Builder;
import ru.cleverhause.devices.dto.device.DeviceControlDto;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;

import java.util.List;

public class DeviceControlResponse extends DeviceControlDto {

    @Builder
    public DeviceControlResponse(Long deviceId, List<SensorControlDto> sensors) {
        super(deviceId, sensors);
    }
}
