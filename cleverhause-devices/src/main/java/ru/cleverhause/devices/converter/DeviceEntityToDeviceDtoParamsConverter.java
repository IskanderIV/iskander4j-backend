package ru.cleverhause.devices.converter;

import com.google.common.base.Converter;
import org.springframework.stereotype.Component;
import ru.cleverhause.devices.dto.device.DeviceParamsDto;
import ru.cleverhause.devices.dto.sensor.SensorParamsDto;
import ru.cleverhause.devices.entity.DeviceParamsEntity;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class DeviceEntityToDeviceDtoParamsConverter extends Converter<DeviceParamsEntity, DeviceParamsDto> {

    @Nullable
    @Override
    protected DeviceParamsDto doForward(DeviceParamsEntity entity) {
        return DeviceParamsDto.<SensorParamsDto>builder()
                .deviceId(entity.getId())
                .deviceName(entity.getDeviceName())
                .username(entity.getUsername())
                .sensors(entity.getSensors().stream()
                        .map(sensorEntity -> SensorParamsDto.builder()
                                .min(sensorEntity.getMin())
                                .max(sensorEntity.getMax())
                                .adj(sensorEntity.getAdj())
                                .discrete(sensorEntity.getDiscrete())
                                .rotate(sensorEntity.getRotate())
                                .signaling(sensorEntity.getSignaling())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Nullable
    @Override
    protected DeviceParamsEntity doBackward(DeviceParamsDto dto) {
        return DeviceParamsEntity.builder()
                .deviceName(dto.getDeviceName())
                .username(dto.getUsername())
                .sensors(dto.getSensors().stream()
                        .map(sensorDto -> DeviceParamsEntity.SensorParamsEntity.builder()
                                .min(sensorDto.getMin())
                                .max(sensorDto.getMax())
                                .adj(sensorDto.getAdj())
                                .discrete(sensorDto.getDiscrete())
                                .rotate(sensorDto.getRotate())
                                .signaling(sensorDto.getSignaling())
                                .build())
                        .collect(Collectors.toSet()))
                .changed(LocalDateTime.now())
                .build();
    }
}
