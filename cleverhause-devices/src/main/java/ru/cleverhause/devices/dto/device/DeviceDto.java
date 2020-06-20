package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.cleverhause.devices.dto.sensor.SensorDto;

import java.io.Serializable;
import java.util.List;

/**
 * Possibly it was created for sending out data for web-api needs
 */
@Getter
public class DeviceDto<T extends SensorDto> implements Serializable {
    private final Long deviceId;
    private final List<T> sensors;

    @Builder
    public DeviceDto(Long deviceId, List<T> sensors) {
        this.deviceId = deviceId;
        this.sensors = sensors;
    }
}
