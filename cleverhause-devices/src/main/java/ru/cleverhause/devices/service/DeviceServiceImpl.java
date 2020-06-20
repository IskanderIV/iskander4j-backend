package ru.cleverhause.devices.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cleverhause.devices.converter.DeviceEntityToDeviceDtoDataConverter;
import ru.cleverhause.devices.converter.DeviceEntityToDeviceDtoParamsConverter;
import ru.cleverhause.devices.dto.device.DeviceControlDto;
import ru.cleverhause.devices.dto.device.DeviceDto;
import ru.cleverhause.devices.dto.request.DeviceDataRequest;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;
import ru.cleverhause.devices.dto.request.DevicesControlRequest;
import ru.cleverhause.devices.dto.response.DeviceControlResponse;
import ru.cleverhause.devices.dto.response.DevicesDataResponse;
import ru.cleverhause.devices.dto.response.UserDevicesResponse;
import ru.cleverhause.devices.dto.sensor.SensorControlDto;
import ru.cleverhause.devices.dto.sensor.SensorDto;
import ru.cleverhause.devices.entity.DeviceDataEntity;
import ru.cleverhause.devices.entity.DeviceParamsEntity;
import ru.cleverhause.devices.repository.DeviceDataDao;
import ru.cleverhause.devices.repository.DeviceParamsDao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceDataDao deviceDataDao;
    private final DeviceParamsDao deviceParamsDao;
    private final DeviceEntityToDeviceDtoParamsConverter paramsConverter;
    private final DeviceEntityToDeviceDtoDataConverter dataConverter;

    @Override
    public UserDevicesResponse findAllByUsername(String username) {
        return new UserDevicesResponse(Optional.ofNullable(deviceParamsDao.findAllByUsername(username))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(paramsConverter::convert)
                .collect(Collectors.toList()));
    }

    @Override
    public DevicesDataResponse findAllByIds(List<String> deviceIds) {
        List<Long> ids = deviceIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        return DevicesDataResponse.builder()
                .devices(getDeviceDataEntityStream(ids)
                        .map(dataConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    private Stream<DeviceDataEntity> getDeviceDataEntityStream(List<Long> ids) {
        return StreamSupport.stream(deviceDataDao.findAllById(ids).spliterator(), false);
    }

    @Transactional
    @Override
    public void updateControl(final DevicesControlRequest devicesControlRequest) {
        final Map<Long, DeviceControlDto> deviceControlMap =
                convertToMap(devicesControlRequest.getDevices(), DeviceControlDto::getDeviceId);
        List<Long> deviceIds = devicesControlRequest.getDevices().stream()
                .map(DeviceDto::getDeviceId)
                .collect(Collectors.toList());
        List<DeviceDataEntity> updatedDevices = getDeviceDataEntityStream(deviceIds)
                .peek(updatedEntity -> {
                    Long deviceId = updatedEntity.getId();
                    if (deviceControlMap.containsKey(deviceId)) {
                        updateDeviceControl(updatedEntity, deviceControlMap.get(deviceId));
                    }
                })
                .collect(Collectors.toList());
        deviceDataDao.saveAll(updatedDevices);
    }

    private void updateDeviceControl(DeviceDataEntity updatedEntity, DeviceControlDto deviceControlDto) {
        List<SensorControlDto> sensors = deviceControlDto.getSensors();
        final Map<Integer, SensorControlDto> sensorControlMap = convertToMap(sensors, SensorDto::getId);
        updatedEntity.getSensors().forEach(updatedSensor -> {
            Integer sensorId = updatedSensor.getId();
            if (sensorControlMap.containsKey(sensorId)) {
                updatedSensor.setCtrlVal(sensorControlMap.get(sensorId).getCtrlVal());
                updatedSensor.setChanged(LocalDateTime.now());
            }
        });
        updatedEntity.setChanged(LocalDateTime.now());
    }

    private <E, T extends Collection<E>, K> Map<K, E> convertToMap(T list, Function<E, K> keyFunction) {
        return list.stream().collect(Collectors.toUnmodifiableMap(keyFunction, e -> e));
    }

    @Transactional
    @Override
    public void deleteDevices(List<String> deviceIds) {
        deviceIds.stream()
                .mapToLong(Long::valueOf)
                .forEach(deviceDataDao::deleteById);
    }

    @Transactional
    @Override
    public void addDevice(DeviceParamsRequest deviceParamsRequest) {
        DeviceParamsEntity deviceParams = paramsConverter.reverse().convert(deviceParamsRequest);
        if (deviceParams != null) {
            if (ObjectUtils.isEmpty(deviceParamsRequest.getDeviceId())) {
                deviceParams.setId(deviceParamsRequest.getDeviceId());
                deviceParamsDao.save(deviceParams);
            } else {
                deviceParams.setCreated(LocalDateTime.now());
                deviceParamsDao.insert(deviceParams);
            }
        }
    }

    @Transactional
    @Override
    public DeviceControlResponse updateDeviceData(DeviceDataRequest deviceDataRequest) {
        DeviceDataEntity deviceData = dataConverter.reverse().convert(deviceDataRequest);
        if (deviceData != null) {
            if (ObjectUtils.isNotEmpty((deviceDataRequest.getDeviceId()))) {
                deviceData.setId(deviceDataRequest.getDeviceId());
                deviceData = deviceDataDao.save(deviceData);
                return DeviceControlResponse.builder()
                        .deviceId(deviceData.getId())
                        .sensors(deviceData.getSensors().stream()
                                .map(sensorDataEntity -> SensorControlDto.builder()
                                        .id(sensorDataEntity.getId())
                                        .ctrlVal(sensorDataEntity.getCtrlVal())
                                        .build())
                                .collect(Collectors.toList()))
                        .build();
            }
        }
        return null;
    }
}
