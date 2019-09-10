package ru.cleverhause.web.api.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/22/2018.
 */
public class OutputDevices<T extends Serializable> implements Serializable {
    private List<T> devices = new ArrayList<>();

    public OutputDevices(List<T> devices) {
        this.devices = devices;
    }

    public OutputDevices() {
    }

    public List<T> getDevices() {
        return devices;
    }

    public void setDevices(List<T> devices) {
        this.devices = devices;
    }
}
