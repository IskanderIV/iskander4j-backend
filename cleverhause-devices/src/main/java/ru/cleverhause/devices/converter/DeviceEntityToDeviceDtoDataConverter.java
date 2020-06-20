package ru.cleverhause.devices.converter;

import com.google.common.base.Converter;
import org.springframework.stereotype.Component;
import ru.cleverhause.devices.dto.device.DeviceDataDto;
import ru.cleverhause.devices.dto.sensor.SensorDataDto;
import ru.cleverhause.devices.entity.DeviceDataEntity;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class DeviceEntityToDeviceDtoDataConverter extends Converter<DeviceDataEntity, DeviceDataDto> {

    @Nullable
    @Override
    protected DeviceDataDto doForward(DeviceDataEntity entity) {
        return DeviceDataDto.<SensorDataDto>builder()
                .deviceId(entity.getId())
                .sensors(entity.getSensors().stream()
                        .map(sensorEntity -> SensorDataDto.builder()
                                .id(sensorEntity.getId())
                                .ack(sensorEntity.getAck())
                                .adj(sensorEntity.getAdj())
                                .ctrlVal(sensorEntity.getCtrlVal())
                                .build()
                        )
                        .collect(Collectors.toList()))
                .deviceErrors(DeviceDataDto.DeviceErrorsDto.builder()
                        .gsm(entity.getErrors().getGsm())
                        .lcd(entity.getErrors().getLcd())
                        .radio(entity.getErrors().getRadio())
                        .build())
                .build();
    }

    @Nullable
    @Override
    protected DeviceDataEntity doBackward(DeviceDataDto dto) {
        return DeviceDataEntity.builder()
                .changed(LocalDateTime.now())
                .sensors(dto.getSensors().stream()
                        .map(sensorDto -> DeviceDataEntity.SensorDataEntity.builder()
                                .id(sensorDto.getId())
                                .ack(sensorDto.getAck())
                                .adj(sensorDto.getAdj())
                                .ctrlVal(sensorDto.getCtrlVal())
                                .build()
                        )
                        .collect(Collectors.toSet()))
                .errors(DeviceDataEntity.DeviceErrorEntity.builder()
                        .gsm(dto.getDeviceErrors().getGsm())
                        .lcd(dto.getDeviceErrors().getLcd())
                        .radio(dto.getDeviceErrors().getRadio())
                        .build())
                .build();
    }
}
