package ru.cleverhause.app.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/18/2018.
 */
@Data
public class BoardDto implements Serializable {
    private Long boardUID;
    private String boardName;
    private int numDevices;
    private List<DeviceDto> devices = Lists.newArrayList();
    private List<DeviceStructure> structureList = Lists.newArrayList();
    private List<DeviceDataRecord> dataRecords = Lists.newArrayList();
    private List<DeviceControl> controlList = Lists.newArrayList();

    public int getNumDevices() {
        return devices.size();
    }
}
