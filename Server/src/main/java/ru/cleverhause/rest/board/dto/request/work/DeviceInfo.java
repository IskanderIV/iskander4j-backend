package ru.cleverhause.rest.board.dto.request.work;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Getter
@Setter
public class DeviceInfo implements Serializable {
    private int id;
    private Double ack;
    private Boolean adj;
    private Double ctrlVal;
    private Boolean radioErr;

    public DeviceInfo(DeviceInfo.Builder builder) {
        this.id = builder.id;
        this.ack = builder.ack;
        this.adj = builder.adj;
        this.ctrlVal = builder.ctrlVal;
        this.radioErr = builder.radioErr;
    }

    public static class Builder {
        private int id;
        private Double ack;
        private Boolean adj;
        private Double ctrlVal;
        private Boolean radioErr;

        public Builder() {
        }

        public DeviceInfo.Builder setId(int id) {
            this.id = id;
            return this;
        }

        public DeviceInfo.Builder setAck(Double ack) {
            this.ack = ack;
            return this;
        }

        public DeviceInfo.Builder setAdj(Boolean adj) {
            this.adj = adj;
            return this;
        }

        public DeviceInfo.Builder setCtrlVal(Double ctrlVal) {
            this.ctrlVal = ctrlVal;
            return this;
        }

        public DeviceInfo.Builder setRadioErr(Boolean radioErr) {
            this.radioErr = radioErr;
            return this;
        }

        public DeviceInfo build() {
            return new DeviceInfo(this);
        }
    }

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
