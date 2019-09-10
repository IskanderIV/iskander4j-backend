package ru.cleverhause.device.api.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public class DeviceData implements Serializable {
    private Long id;
    private Double ack;
    private Boolean adj; //POS
    private Double ctrlVal; // POS
    private Boolean radioErr;

    public DeviceData(Long id, Double ack, Boolean adj, Double ctrlVal, Boolean radioErr) {
        this.id = id;
        this.ack = ack;
        this.adj = adj;
        this.ctrlVal = ctrlVal;
        this.radioErr = radioErr;
    }

    public DeviceData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAck() {
        return ack;
    }

    public void setAck(Double ack) {
        this.ack = ack;
    }

    public Boolean getAdj() {
        return adj;
    }

    public void setAdj(Boolean adj) {
        this.adj = adj;
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

    private DeviceData(DeviceData.Builder builder) {
        this.id = builder.id;
        this.ack = builder.ack;
        this.adj = builder.adj;
        this.ctrlVal = builder.ctrlVal;
        this.radioErr = builder.radioErr;
    }

    public static class Builder {
        private Long id;
        private Double ack;
        private Boolean adj;
        private Double ctrlVal;
        private Boolean radioErr;

        public Builder() {
        }

        public DeviceData.Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public DeviceData.Builder setAck(Double ack) {
            this.ack = ack;
            return this;
        }

        public DeviceData.Builder setAdj(Boolean adj) {
            this.adj = adj;
            return this;
        }

        public DeviceData.Builder setCtrlVal(Double ctrlVal) {
            this.ctrlVal = ctrlVal;
            return this;
        }

        public DeviceData.Builder setRadioErr(Boolean radioErr) {
            this.radioErr = radioErr;
            return this;
        }

        public DeviceData build() {
            return new DeviceData(this);
        }
    }

    @Override
    public String toString() {
        return "DeviceData{" +
                "deviceId=" + id +
                ", deviceAck=" + ack +
                ", adjustable=" + adj +
                ", controlValue=" + ctrlVal +
                ", deviceErrors=" + radioErr +
                '}';
    }
}
