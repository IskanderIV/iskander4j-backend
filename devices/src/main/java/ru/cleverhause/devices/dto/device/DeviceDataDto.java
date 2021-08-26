package ru.cleverhause.devices.dto.device;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class DeviceDataDto extends DeviceDto<SensorDataDto> {

    @Valid
    @NotNull
    private final DeviceErrorsDto deviceErrors;

    @Builder(builderMethodName = "deviceDataDtoBuilder")
    @JsonCreator
    public DeviceDataDto(@JsonProperty("deviceId") String deviceId,
                         @JsonProperty("sensors") List<SensorDataDto> sensors,
                         @JsonProperty("deviceErrors") DeviceErrorsDto deviceErrors) {
        super(deviceId, sensors);
        this.deviceErrors = deviceErrors;
    }

    @Getter
    @Builder
    public static class DeviceErrorsDto implements Serializable {
        @NotNull
        private final Boolean gsm;
        @NotNull
        private final Boolean lcd;
        @NotNull
        private final Boolean radio;

        @JsonCreator
        public DeviceErrorsDto(@JsonProperty("gsm") Boolean gsm,
                               @JsonProperty("lcd") Boolean lcd,
                               @JsonProperty("radio") Boolean radio) {
            this.gsm = gsm;
            this.lcd = lcd;
            this.radio = radio;
        }
    }
}
