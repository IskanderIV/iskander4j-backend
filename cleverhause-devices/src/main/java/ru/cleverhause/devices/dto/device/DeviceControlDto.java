package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;

import java.util.List;

@Getter
public class DeviceControlDto extends DeviceDto<SensorControlDto> {

    @Builder
    public DeviceControlDto(Long deviceId,
                            List<SensorControlDto> sensors) {
        super(deviceId, sensors);
    }
}
