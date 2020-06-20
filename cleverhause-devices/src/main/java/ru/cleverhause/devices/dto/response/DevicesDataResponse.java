package ru.cleverhause.devices.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.cleverhause.devices.dto.device.DeviceDataDto;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class DevicesDataResponse implements Serializable {
    private final @NonNull List<DeviceDataDto> devices;
}
