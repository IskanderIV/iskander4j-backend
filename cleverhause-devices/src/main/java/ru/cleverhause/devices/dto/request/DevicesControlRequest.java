package ru.cleverhause.devices.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import ru.cleverhause.devices.dto.device.DeviceControlDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Getter
@ToString
public class DevicesControlRequest implements Serializable {
    @NotEmpty
    private final List<@Valid DeviceControlDto> devices;

    @JsonCreator
    public DevicesControlRequest(@JsonProperty("devices") List<DeviceControlDto> devices) {
        this.devices = devices;
    }
}
