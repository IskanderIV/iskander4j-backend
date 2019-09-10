package ru.cleverhause.device.api.dto;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/19/2018.
 */

public class DeviceDataRecord implements Serializable {
    private Long id;
    private List<DeviceData> deviceDataList = Lists.newArrayList();
    private Date created;

    public DeviceDataRecord(Long id, List<DeviceData> deviceDataList, Date created) {
        this.id = id;
        this.deviceDataList = deviceDataList;
        this.created = created;
    }

    public DeviceDataRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DeviceData> getDeviceDataList() {
        return deviceDataList;
    }

    public void setDeviceDataList(List<DeviceData> deviceDataList) {
        this.deviceDataList = deviceDataList;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "DeviceDataRecord{" +
                "id=" + id +
                ", deviceDataList=" + deviceDataList +
                ", created=" + created +
                '}';
    }
}
