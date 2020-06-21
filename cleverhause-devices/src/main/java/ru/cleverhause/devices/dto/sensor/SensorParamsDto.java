package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class SensorParamsDto extends SensorDto {
    private final Double min;
    private final Double max;
    private final Double discrete;
    private final Boolean adj;
    private final Boolean rotate;
    private final Boolean signaling;

    @Builder(builderMethodName = "sensorParamsDtoBuilder")
    public SensorParamsDto(int id, Double min, Double max, Double discrete, Boolean adj, Boolean rotate, Boolean signaling) {
        super(id);
        this.min = min;
        this.max = max;
        this.discrete = discrete;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }
}
