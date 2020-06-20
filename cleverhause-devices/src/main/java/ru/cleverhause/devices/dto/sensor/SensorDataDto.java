package ru.cleverhause.devices.dto.sensor;

import lombok.Builder;
import lombok.Getter;

/**
 * Input dto. Need to transfer sensor data
 */

@Getter
public class SensorDataDto extends SensorDto {
    private final Double ack;
    private final Boolean adj; //PFB
    private final Double ctrlVal; // PFB
    private final Boolean radioErr;

    @Builder
    public SensorDataDto(int id, Double ack, Boolean adj, Double ctrlVal, Boolean radioErr) {
        super(id);
        this.ack = ack;
        this.adj = adj;
        this.ctrlVal = ctrlVal;
        this.radioErr = radioErr;
    }
}
