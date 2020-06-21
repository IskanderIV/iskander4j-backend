package ru.cleverhause.devices.dto.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class SensorDto implements Serializable {
    private final Integer id;
}
