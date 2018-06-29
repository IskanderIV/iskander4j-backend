package ru.cleverhause.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DeviceStructure implements Serializable {
    private Long id;
    private int min;
    private int max;
    private Double discrete;
    private Boolean adj;
    private Boolean rotate;
    private Boolean signaling;

    public DeviceStructure(Long id, int min, int max, Double discrete, Boolean adj, Boolean rotate, Boolean signaling) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.discrete = discrete;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }

    public DeviceStructure(Builder builder) {
        this.id = builder.id;
        this.min = builder.min;
        this.max = builder.max;
        this.discrete = builder.discrete;
        this.adj = builder.adj;
        this.rotate = builder.rotate;
        this.signaling = builder.signaling;
    }

    public DeviceStructure() {
    }

    public static class Builder {
        private Long id;
        private int min;
        private int max;
        private Double discrete;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        public Builder(Long id, int min, int max, Double discrete, Boolean adj, Boolean rotate, Boolean signaling) {
            this.id = id;
            this.min = min;
            this.max = max;
            this.discrete = discrete;
            this.adj = adj;
            this.rotate = rotate;
            this.signaling = signaling;
        }

        public DeviceStructure.Builder setId(Long id) {
            return this;
        }

        public DeviceStructure.Builder setMin(int min) {
            return this;
        }

        public DeviceStructure.Builder setMax(int max) {
            return this;
        }

        public DeviceStructure.Builder setDiscrete(Double discrete) {
            return this;
        }

        public DeviceStructure.Builder setAdj(Boolean adj) {
            return this;
        }

        public DeviceStructure.Builder setRotate(Boolean rotate) {
            return this;
        }

        public DeviceStructure.Builder setSignaling(Boolean signaling) {
            return this;
        }

        public DeviceStructure build() {
            return new DeviceStructure(this);
        }
    }

    @Override
    public String toString() {
        return "DeviceStructure{" +
                "id=" + id +
                ", min=" + min +
                ", max=" + max +
                ", discrete=" + discrete +
                ", adj=" + adj +
                ", rotate=" + rotate +
                ", signaling=" + signaling +
                '}';
    }
}
