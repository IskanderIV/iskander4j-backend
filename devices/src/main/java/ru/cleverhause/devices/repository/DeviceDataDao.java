package ru.cleverhause.devices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cleverhause.devices.entity.DeviceDataEntity;

public interface DeviceDataDao extends MongoRepository<DeviceDataEntity, String> {
}
