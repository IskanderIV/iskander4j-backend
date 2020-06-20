package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;

/**
 * Output dto. Need to transfer sensor to some state
 */
@Getter
public class SensorSettingDto extends SensorControlDto {
    private final Boolean adj;
    private final Boolean rotate;
    private final Boolean signaling;

    @Builder
    public SensorSettingDto(int id, Double ctrlVal, Boolean adj, Boolean rotate, Boolean signaling) {
        super(id, ctrlVal);
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }
}
