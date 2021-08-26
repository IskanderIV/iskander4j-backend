package ru.cleverhause.devices.dto.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public SensorParamsDto(@JsonProperty("id") int id,
                           @JsonProperty("min") Double min,
                           @JsonProperty("max") Double max,
                           @JsonProperty("discrete") Double discrete,
                           @JsonProperty("adj") Boolean adj,
                           @JsonProperty("rotate") Boolean rotate,
                           @JsonProperty("signaling") Boolean signaling) {
        super(id);
        this.min = min;
        this.max = max;
        this.discrete = discrete;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }
}
