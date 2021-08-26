package ru.cleverhause.devices.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceDataDto;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;

import javax.validation.Valid;
import java.util.List;

@Valid
@Getter
@ToString(callSuper = true)
public class DeviceDataRequest extends DeviceDataDto {

    @Builder(builderMethodName = "deviceDataRequestBuilder")
    public DeviceDataRequest(String deviceId, List<SensorDataDto> sensors, DeviceErrorsDto deviceErrors) {
        super(deviceId, sensors, deviceErrors);
    }
}
