package ru.cleverhause.devices.dto.request;

import lombok.Data;
import lombok.NonNull;
import ru.cleverhause.devices.dto.device.DeviceControlDto;

import java.io.Serializable;
import java.util.List;

@Data
public class DevicesControlRequest implements Serializable {
    private final @NonNull List<DeviceControlDto> devices;
}
