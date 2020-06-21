package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class DeviceDataDto extends DeviceDto<SensorDataDto> {

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
        private final Boolean gsm;
        private final Boolean lcd;
        private final Boolean radio;
    }
}
