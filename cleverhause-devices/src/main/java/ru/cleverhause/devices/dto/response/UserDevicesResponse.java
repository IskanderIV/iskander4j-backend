package ru.cleverhause.devices.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import ru.cleverhause.devices.dto.device.DeviceParamsDto;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDevicesResponse implements Serializable {
    private @NonNull List<DeviceParamsDto> devices;

    @JsonCreator
    public UserDevicesResponse(@JsonProperty("devices") List<DeviceParamsDto> devices) {
        this.devices = devices;
    }
}
