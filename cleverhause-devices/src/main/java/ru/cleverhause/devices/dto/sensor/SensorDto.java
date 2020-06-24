package ru.cleverhause.devices.dto.sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class SensorDto implements Serializable {
    @NotNull
    @Positive
    @Min(1)
    @Max(8)
    private final Integer id;
}
