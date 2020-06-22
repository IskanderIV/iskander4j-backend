package ru.cleverhause.devices.dto.device;

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
    public DeviceDataDto(String deviceId,
                         List<SensorDataDto> sensors,
                         DeviceErrorsDto deviceErrors) {
        super(deviceId, sensors);
        this.deviceErrors = deviceErrors;
    }

    @Data
    @Builder
    public static class DeviceErrorsDto implements Serializable {
        @NotNull
        private final Boolean gsm;
        @NotNull
        private final Boolean lcd;
        @NotNull
        private final Boolean radio;
    }
}
