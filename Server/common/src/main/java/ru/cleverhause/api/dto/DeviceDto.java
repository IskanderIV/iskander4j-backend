package ru.cleverhause.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/18/2018.
 */
@Data
public class DeviceDto implements Serializable {
    private DeviceStructure structure;
    private DeviceData data;
    private DeviceControl control;
}
