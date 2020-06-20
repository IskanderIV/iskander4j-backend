package ru.cleverhause.devices.dto.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
public class SensorDto implements Serializable {
    private final Integer id;
}
