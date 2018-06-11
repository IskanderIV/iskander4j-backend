package ru.cleverhause.rest.board.dto.request.registration;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */
public class DeviceSetting implements Serializable {
    private int id;
    private Boolean adj;
    private Boolean rotate;
    private Boolean signaling;

    public DeviceSetting(DeviceSetting.Builder builder) {
        this.id = builder.id;
        this.adj = builder.adj;
        this.rotate = builder.rotate;
        this.signaling = builder.signaling;
    }

    public static class Builder {
        private int id;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        public Builder() {
        }

        public DeviceSetting.Builder setId(int id) {
            this.id = id;
            return this;
        }

        public DeviceSetting.Builder setAdj(Boolean adj) {
            this.adj = adj;
            return this;
        }

        public DeviceSetting.Builder setRotate(Boolean rotate) {
            this.rotate = rotate;
            return this;
        }

        public DeviceSetting.Builder setSignaling(Boolean signaling) {
            this.signaling = signaling;
            return this;
        }

        public DeviceSetting build() {
            return new DeviceSetting(this);
        }
    }

    @Override
    public String toString() {
        return "DeviceSetting{" +
                "id=" + id +
                ", adj=" + adj +
                ", rotate=" + rotate +
                ", signaling=" + signaling +
                '}';
    }
}
