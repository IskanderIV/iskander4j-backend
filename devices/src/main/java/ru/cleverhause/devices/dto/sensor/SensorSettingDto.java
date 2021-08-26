package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class SensorSettingDto extends SensorControlDto {
    @NotNull
    private final Boolean adj;
    @NotNull
    private final Boolean rotate;
    @NotNull
    private final Boolean signaling;

    @Builder(builderMethodName = "sensorSettingDtoBuilder")
    public SensorSettingDto(int id, Double ctrlVal, Boolean adj, Boolean rotate, Boolean signaling) {
        super(id, ctrlVal);
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }
}
