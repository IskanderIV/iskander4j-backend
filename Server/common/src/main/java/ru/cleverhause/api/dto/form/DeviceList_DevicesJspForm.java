package ru.cleverhause.api.dto.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/16/2018.
 */
@Data
public class DeviceList_DevicesJspForm implements Serializable {
    private List<Device_DevicesJspForm> devices;
}
