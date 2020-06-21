package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SensorControlDto extends SensorDto {
    private final Double ctrlVal;

    @Builder(builderMethodName = "sensorControlDtoBuilder")
    public SensorControlDto(int id, Double ctrlVal) {
        super(id);
        this.ctrlVal = ctrlVal;
    }
}
