package ru.cleverhause.devices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cleverhause.devices.entity.DeviceParamsEntity;

import java.util.List;

public interface DeviceParamsDao extends MongoRepository<DeviceParamsEntity, Long> {

    List<DeviceParamsEntity> findAllByUsername(String username);
}
