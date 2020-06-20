package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Getter;
import ru.cleverhause.devices.dto.sensor.SensorDto;
import ru.cleverhause.devices.dto.sensor.SensorParamsDto;

import java.util.List;

@Getter
public class DeviceParamsDto extends DeviceDto<SensorParamsDto> {
    private final String deviceName;
    //if input then copy here from principal on zuul side
    private final String username;

    @Builder
    public DeviceParamsDto(Long deviceId,
                           List<SensorParamsDto> sensors,
                           String deviceName,
                           String username) {
        super(deviceId, sensors);
        this.deviceName = deviceName;
        this.username = username;
    }
}
