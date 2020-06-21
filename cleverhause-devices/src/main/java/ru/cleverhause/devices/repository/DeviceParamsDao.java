package ru.cleverhause.devices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cleverhause.devices.entity.DeviceParamsEntity;

import java.util.List;
import java.util.Optional;

public interface DeviceParamsDao extends MongoRepository<DeviceParamsEntity, String> {

    List<DeviceParamsEntity> findAllByUsername(String username);

    boolean existsByDeviceNameAndUsername(String deviceName, String username);
}
