package ru.cleverhause.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeviceControl implements Serializable {
    private Long id;
    private Double ctrlVal;

    public DeviceControl(Long id, Double ctrlVal) {
        this.id = id;
        this.ctrlVal = ctrlVal;
    }

    public DeviceControl() {
    }

    @Override
    public String toString() {
        return "DeviceControl{" +
                "id=" + id +
                ", ctrlVal=" + ctrlVal +
                '}';
    }
}
