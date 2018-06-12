package ru.cleverhause.rest.frontend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceControl {
    private int id;
    private Double control;

    public DeviceControl(int id, Double control) {
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
