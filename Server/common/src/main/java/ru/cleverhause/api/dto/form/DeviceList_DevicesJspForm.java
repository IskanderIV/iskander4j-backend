package ru.cleverhause.api.dto.form;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/16/2018.
 */

public class DeviceList_DevicesJspForm implements Serializable {
    private List<DeviceOnDevicesJspForm> devices;

    public DeviceList_DevicesJspForm(List<DeviceOnDevicesJspForm> devices) {
        this.devices = devices;
    }

    public DeviceList_DevicesJspForm() {
    }

    public List<DeviceOnDevicesJspForm> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceOnDevicesJspForm> devices) {
        this.devices = devices;
    }
}
