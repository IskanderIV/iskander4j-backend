package ru.cleverhause.rest.frontend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeviceControl implements Serializable {
    private Long id;
    private Double control;

    public DeviceControl(Long id, Double control) {
        this.id = id;
        this.control = control;
    }

    public DeviceControl() {
    }

    @Override
    public String toString() {
        return "DeviceControl{" +
                "id=" + id +
                ", control=" + control +
                '}';
    }
}
