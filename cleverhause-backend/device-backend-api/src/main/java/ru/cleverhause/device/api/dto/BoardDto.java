package ru.cleverhause.device.api.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/18/2018.
 */

public class BoardDto implements Serializable {
    private Long boardUID;
    private String boardName;
    private int numDevices;
    private List<DeviceDto> devices;
    private List<DeviceStructure> structureList;
    private List<DeviceDataRecord> dataRecords;
    private List<DeviceControl> controlList;

    public BoardDto(Long boardUID,
                    String boardName,
                    int numDevices,
                    List<DeviceDto> devices,
                    List<DeviceStructure> structureList,
                    List<DeviceDataRecord> dataRecords,
                    List<DeviceControl> controlList) {
        this.boardUID = boardUID;
        this.boardName = boardName;
        this.numDevices = numDevices;
        this.devices = devices;
        this.structureList = structureList;
        this.dataRecords = dataRecords;
        this.controlList = controlList;
    }

    public BoardDto() {
    }

    public Long getBoardUID() {
        return boardUID;
    }

    public void setBoardUID(Long boardUID) {
        this.boardUID = boardUID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public void setNumDevices(int numDevices) {
        this.numDevices = numDevices;
    }

    public List<DeviceDto> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDto> devices) {
        this.devices = devices;
    }

    public List<DeviceStructure> getStructureList() {
        return structureList;
    }

    public void setStructureList(List<DeviceStructure> structureList) {
        this.structureList = structureList;
    }

    public List<DeviceDataRecord> getDataRecords() {
        return dataRecords;
    }

    public void setDataRecords(List<DeviceDataRecord> dataRecords) {
        this.dataRecords = dataRecords;
    }

    public List<DeviceControl> getControlList() {
        return controlList;
    }

    public void setControlList(List<DeviceControl> controlList) {
        this.controlList = controlList;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "boardUID=" + boardUID +
                ", boardName='" + boardName + '\'' +
                ", numDevices=" + numDevices +
                ", devices=" + devices +
                ", structureList=" + structureList +
                ", dataRecords=" + dataRecords +
                ", controlList=" + controlList +
                '}';
    }
}
