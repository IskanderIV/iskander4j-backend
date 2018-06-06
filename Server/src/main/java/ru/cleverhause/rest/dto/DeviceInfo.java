package ru.cleverhause.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeviceInfo implements Serializable {
    private int id;
    private Double ack;
    private Boolean adj;
    private Double ctrlVal;
    private Boolean radioErr;

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceId=" + id +
                ", deviceAck=" + ack +
                ", adjustable=" + adj +
                ", controlValue=" + ctrlVal +
                ", deviceErrors=" + radioErr +
                '}';
    }
}
