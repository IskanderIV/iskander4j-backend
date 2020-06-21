package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class DeviceControlDto extends DeviceDto<SensorControlDto> {

    @Builder(builderMethodName = "deviceControlBuilder")
    public DeviceControlDto(String deviceId,
                            List<SensorControlDto> sensors) {
        super(deviceId, sensors);
    }
}
