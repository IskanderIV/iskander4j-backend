package ru.cleverhause.devices.dto.response;

import lombok.Data;
import lombok.NonNull;
import ru.cleverhause.devices.dto.device.DeviceParamsDto;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDevicesResponse implements Serializable {
    private @NonNull List<DeviceParamsDto> devices;
}
