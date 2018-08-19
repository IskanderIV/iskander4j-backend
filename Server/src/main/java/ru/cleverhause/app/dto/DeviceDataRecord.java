package ru.cleverhause.app.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/19/2018.
 */
@Data
public class DeviceDataRecord implements Serializable {
    private Long id;
    private List<DeviceData> deviceDataList = Lists.newArrayList();
    private Date created;
}
