package ru.cleverhause.devices.dto.sensor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonCreator
    public SensorDataDto(@JsonProperty("id") int id,
                         @JsonProperty("ack") Double ack,
                         @JsonProperty("adj") Boolean adj,
                         @JsonProperty("ctrlVal") Double ctrlVal,
                         @JsonProperty("radioErr") Boolean radioErr) {
        super(id);
        this.ack = ack;
        this.adj = adj;
        this.ctrlVal = ctrlVal;
        this.radioErr = radioErr;
    }
}
