package ru.cleverhause.devices.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import ru.cleverhause.devices.dto.device.DeviceDataDto;

import java.io.Serializable;
import java.util.List;

@Data
public class DevicesDataResponse implements Serializable {
    private final @NonNull List<DeviceDataDto> devices;

    @JsonCreator
    public DevicesDataResponse(@JsonProperty("devices") List<DeviceDataDto> devices) {
        this.devices = devices;
    }
}
