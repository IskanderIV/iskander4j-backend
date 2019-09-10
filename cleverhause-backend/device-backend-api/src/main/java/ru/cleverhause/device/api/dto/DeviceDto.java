package ru.cleverhause.device.api.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/18/2018.
 */

public class DeviceDto implements Serializable {
    private DeviceStructure structure;
    private DeviceData data;
    private DeviceControl control;

    public DeviceDto(DeviceStructure structure, DeviceData data, DeviceControl control) {
        this.structure = structure;
        this.data = data;
        this.control = control;
    }

    public DeviceStructure getStructure() {
        return structure;
    }

    public void setStructure(DeviceStructure structure) {
        this.structure = structure;
    }

    public DeviceData getData() {
        return data;
    }

    public void setData(DeviceData data) {
        this.data = data;
    }

    public DeviceControl getControl() {
        return control;
    }

    public void setControl(DeviceControl control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "DeviceDto{" +
                "structure=" + structure +
                ", data=" + data +
                ", control=" + control +
                '}';
    }
}
