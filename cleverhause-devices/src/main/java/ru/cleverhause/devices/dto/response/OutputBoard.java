package ru.cleverhause.devices.dto.response;

import ru.cleverhause.device.api.dto.Errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/22/2018.
 */
public class OutputBoard<T extends Serializable> implements Serializable {
    private List<T> devices = new ArrayList<>();
    private Errors errors;

    public OutputBoard(List<T> devices, Errors errors) {
        this.devices = devices;
        this.errors = errors;
    }

    public List<T> getDevices() {
        return devices;
    }

    public void setDevices(List<T> devices) {
        this.devices = devices;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
