package ru.cleverhause.devices.dto.sensor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class SensorControlDto extends SensorDto {
    @NotNull
    private final Double ctrlVal;

    @Builder(builderMethodName = "sensorControlDtoBuilder")
    @JsonCreator
    public SensorControlDto(@JsonProperty("id") int id,
                            @JsonProperty("ctrlVal") Double ctrlVal) {
        super(id);
        this.ctrlVal = ctrlVal;
    }
}
