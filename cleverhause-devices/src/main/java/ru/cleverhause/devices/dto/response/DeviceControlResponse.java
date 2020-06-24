package ru.cleverhause.devices.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceControlDto;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;

import java.util.List;

@ToString(callSuper = true)
public class DeviceControlResponse extends DeviceControlDto {

    @Builder(builderMethodName = "deviceControlResponseBuilder")
    @JsonCreator
    public DeviceControlResponse(@JsonProperty("deviceId") String deviceId,
                                 @JsonProperty("sensors") List<SensorControlDto> sensors) {
        super(deviceId, sensors);
    }
}
