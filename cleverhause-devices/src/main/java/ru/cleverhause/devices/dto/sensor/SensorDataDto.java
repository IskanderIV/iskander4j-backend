package ru.cleverhause.devices.dto.sensor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
public class SensorDataDto extends SensorDto {
    @NotNull
    private final Double ack;
    @NotNull
    private final Boolean adj; //PFB
    @NotNull
    private final Double ctrlVal; // PFB
    @NotNull
    private final Boolean radioErr;

    @Builder(builderMethodName = "sensorDataDtoBuilder")
    public SensorDataDto(int id, Double ack, Boolean adj, Double ctrlVal, Boolean radioErr) {
        super(id);
        this.ack = ack;
        this.adj = adj;
        this.ctrlVal = ctrlVal;
        this.radioErr = radioErr;
    }
}
