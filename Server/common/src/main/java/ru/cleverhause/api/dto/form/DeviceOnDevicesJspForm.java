package ru.cleverhause.api.dto.form;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/15/2018.
 */

public class DeviceOnDevicesJspForm implements Serializable {
    private Long id;
    private Boolean adj = false;
    private Boolean rotate = false;
    private Boolean signaling = false;
    private Double ack = 0.0;
    private Double discrete = 0.1;
    private Double ctrlVal = 0.0;
    private Boolean radioErr = false;
    private Boolean dataError = false;
    private Boolean controlError = false;

    public DeviceOnDevicesJspForm(Long id,
                                  Boolean adj,
                                  Boolean rotate,
                                  Boolean signaling,
                                  Double ack,
                                  Double discrete,
                                  Double ctrlVal,
                                  Boolean radioErr,
                                  Boolean dataError,
                                  Boolean controlError) {
        this.id = id;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
        this.ack = ack;
        this.discrete = discrete;
        this.ctrlVal = ctrlVal;
        this.radioErr = radioErr;
        this.dataError = dataError;
        this.controlError = controlError;
    }

    public DeviceOnDevicesJspForm() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdj() {
        return adj;
    }

    public void setAdj(Boolean adj) {
        this.adj = adj;
    }

    public Boolean getRotate() {
        return rotate;
    }

    public void setRotate(Boolean rotate) {
        this.rotate = rotate;
    }

    public Boolean getSignaling() {
        return signaling;
    }

    public void setSignaling(Boolean signaling) {
        this.signaling = signaling;
    }

    public Double getAck() {
        return ack;
    }

    public void setAck(Double ack) {
        this.ack = ack;
    }

    public Double getDiscrete() {
        return discrete;
    }

    public void setDiscrete(Double discrete) {
        this.discrete = discrete;
    }

    public Double getCtrlVal() {
        return ctrlVal;
    }

    public void setCtrlVal(Double ctrlVal) {
        this.ctrlVal = ctrlVal;
    }

    public Boolean getRadioErr() {
        return radioErr;
    }

    public void setRadioErr(Boolean radioErr) {
        this.radioErr = radioErr;
    }

    public Boolean getDataError() {
        return dataError;
    }

    public void setDataError(Boolean dataError) {
        this.dataError = dataError;
    }

    public Boolean getControlError() {
        return controlError;
    }

    public void setControlError(Boolean controlError) {
        this.controlError = controlError;
    }

    @Override
    public String toString() {
        return "DeviceOnDevicesJspForm{" +
                "id=" + id +
                ", adj=" + adj +
                ", rotate=" + rotate +
                ", signaling=" + signaling +
                ", ack=" + ack +
                ", discrete=" + discrete +
                ", ctrlVal=" + ctrlVal +
                ", radioErr=" + radioErr +
                ", dataError=" + dataError +
                ", controlError=" + controlError +
                '}';
    }
}
