package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;

/**
 * Possibly it was created for device-api
 */
@Getter
public class SensorControlDto extends SensorDto {
    private final Double ctrlVal;

    @Builder
    public SensorControlDto(int id, Double ctrlVal) {
        super(id);
        this.ctrlVal = ctrlVal;
    }
}
