package ru.cleverhause.devices.service;

import ru.cleverhause.devices.dto.request.DeviceDataRequest;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;
import ru.cleverhause.devices.dto.request.DevicesControlRequest;
import ru.cleverhause.devices.dto.response.DeviceControlResponse;
import ru.cleverhause.devices.dto.response.DevicesDataResponse;
import ru.cleverhause.devices.dto.response.UserDevicesResponse;

import java.util.List;

public interface DeviceService {

    UserDevicesResponse findAllByUsername(String username);

    DevicesDataResponse findAllByIds(List<String> deviceIds);

    void updateControl(DevicesControlRequest devicesControlRequest);

    void deleteDevices(List<String> deviceIds);

    void addDevice(DeviceParamsRequest deviceParamsRequest);

    DeviceControlResponse updateDeviceData(DeviceDataRequest deviceDataRequest);
}
