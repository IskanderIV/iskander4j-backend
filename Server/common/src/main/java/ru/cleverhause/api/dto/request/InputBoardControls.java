package ru.cleverhause.api.dto.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/22/2018.
 */
public class InputBoardControls<T extends Serializable> implements Serializable {
    private List<T> devices = new ArrayList<>();

    public InputBoardControls(List<T> devices) {
        this.devices = devices;
    }

    public InputBoardControls() {
    }

    public List<T> getDevices() {
        return devices;
    }

    public void setDevices(List<T> devices) {
        this.devices = devices;
    }
}
