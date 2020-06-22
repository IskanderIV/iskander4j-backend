package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class SensorParamsDto extends SensorDto {
    @NotNull
    private final Double min;
    @NotNull
    private final Double max;
    @NotNull
    private final Double discrete;
    @NotNull
    private final Boolean adj;
    @NotNull
    private final Boolean rotate;
    @NotNull
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
