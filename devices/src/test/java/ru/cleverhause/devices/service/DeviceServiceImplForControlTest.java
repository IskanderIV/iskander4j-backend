package ru.cleverhause.devices.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.cleverhause.devices.dto.device.DeviceControlDto;
import ru.cleverhause.devices.dto.request.DevicesControlRequest;
import ru.cleverhause.devices.repository.DeviceDataDao;
import ru.cleverhause.devices.testcontainer.MongoContainerExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;

@Slf4j
@ExtendWith({SpringExtension.class, MongoContainerExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeviceServiceImplForControlTest {

    @Autowired
    private DeviceServiceImpl service;

    @MockBean
    private DeviceDataDao dataDao;

    @BeforeEach
    void setUp() {

    }

    @Test
    void updateDevicesControlIfDeviceDataDoesNotExistInDataBaseTest() {
        DevicesControlRequest devicesControlRequest =
                new DevicesControlRequest(Collections.singletonList(
                        DeviceControlDto.deviceControlBuilder()
                                .deviceId("someNotExistDeviseId").build()
                ));
        service.updateControl(devicesControlRequest);

        Mockito.verify(dataDao, never()).saveAll(anyList());
    }
}