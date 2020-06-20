package ru.cleverhause.devices.dto.device;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;
import ru.cleverhause.devices.dto.sensor.SensorDto;

import java.io.Serializable;
import java.util.List;

/**
 * Possibly it was created for sending out data for web-api needs
 */
@Getter
public class DeviceDataDto extends DeviceDto<SensorDataDto> {

    private final DeviceErrorsDto deviceErrors;

    @Builder
    public DeviceDataDto(Long deviceId,
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
