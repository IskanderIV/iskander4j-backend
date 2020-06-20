package ru.cleverhause.devices.dto.request;

import lombok.Builder;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceDataDto;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;

import java.util.List;

@ToString(callSuper = true)
public class DeviceDataRequest extends DeviceDataDto {

    @Builder
    public DeviceDataRequest(Long deviceId, List<SensorDataDto> sensors, DeviceErrorsDto deviceErrors) {
        super(deviceId, sensors, deviceErrors);
    }
}
