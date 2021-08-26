package ru.cleverhause.devices.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.sensor.SensorDto;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class DeviceDto<T extends SensorDto> implements Serializable {
    private final String deviceId;
//    @ValidSensors
    private final List<@Valid T> sensors;
}
