package ru.cleverhause.devices.dto.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.sensor.SensorParamsDto;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class DeviceParamsDto extends DeviceDto<SensorParamsDto> {
    private final String deviceName;
    //if input then copy here from principal on zuul side
    private final String username;

    @Builder(builderMethodName = "deviceParamsDtoBuilder")
    @JsonCreator
    public DeviceParamsDto(@JsonProperty("deviceId") String deviceId,
                           @JsonProperty("sensors") List<SensorParamsDto> sensors,
                           @JsonProperty("deviceName") String deviceName,
                           @JsonProperty("username") String username) {
        super(deviceId, sensors);
        this.deviceName = deviceName;
        this.username = username;
    }
}
