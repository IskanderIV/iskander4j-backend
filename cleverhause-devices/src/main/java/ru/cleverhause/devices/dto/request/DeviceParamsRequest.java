package ru.cleverhause.devices.dto.request;

import lombok.Builder;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceParamsDto;
import ru.cleverhause.devices.dto.sensor.SensorParamsDto;

import java.util.List;

@ToString(callSuper = true)
public class DeviceParamsRequest extends DeviceParamsDto {

    @Builder
    public DeviceParamsRequest(Long deviceId, List<SensorParamsDto> sensors, String deviceName, String username) {
        super(deviceId, sensors, deviceName, username);
    }
}
