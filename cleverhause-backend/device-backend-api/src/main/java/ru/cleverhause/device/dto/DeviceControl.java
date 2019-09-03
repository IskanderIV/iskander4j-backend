package ru.cleverhause.device.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/18/2018.
 */

public class DeviceControl implements Serializable {
    private Long id;
    private Double ctrlVal;

    public DeviceControl(Long id, Double ctrlVal) {
        this.id = id;
        this.ctrlVal = ctrlVal;
    }

    public DeviceControl() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCtrlVal() {
        return ctrlVal;
    }

    public void setCtrlVal(Double ctrlVal) {
        this.ctrlVal = ctrlVal;
    }

    @Override
    public String toString() {
        return "DeviceControl{" +
                "id=" + id +
                ", ctrlVal=" + ctrlVal +
                '}';
    }
}
