package ru.cleverhause.device.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */

public class DeviceStructure implements Serializable {
    private final Long id;
    private final Double min;
    private final Double max;
    private final Double discrete;
    private final Boolean adj;
    private final Boolean rotate;
    private final Boolean signaling;

    public DeviceStructure(Long id,
                           Double min,
                           Double max,
                           Double discrete,
                           Boolean adj,
                           Boolean rotate,
                           Boolean signaling) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.discrete = discrete;
        this.adj = adj;
        this.rotate = rotate;
        this.signaling = signaling;
    }

    private DeviceStructure(Builder builder) {
        this.id = builder.id;
        this.min = builder.min;
        this.max = builder.max;
        this.discrete = builder.discrete;
        this.adj = builder.adj;
        this.rotate = builder.rotate;
        this.signaling = builder.signaling;
    }

    public Long getId() {
        return id;
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getDiscrete() {
        return discrete;
    }

    public Boolean getAdj() {
        return adj;
    }

    public Boolean getRotate() {
        return rotate;
    }

    public Boolean getSignaling() {
        return signaling;
    }

    public static class Builder {
        private Long id;
        private Double min;
        private Double max;
        private Double discrete;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        private Builder() {
            this.id = null;
            this.min = 0.0;
            this.max = 1.0;
            this.discrete = 0.1;
            this.adj = true;
            this.rotate = false;
            this.signaling = true;
        }

        public Builder(DeviceStructure structure) {
            this.id = structure.id;
            this.min = structure.min;
            this.max = structure.max;
            this.discrete = structure.discrete;
            this.adj = structure.adj;
            this.rotate = structure.rotate;
            this.signaling = structure.signaling;
        }

        public static Builder create() {
            return new Builder();
        }

        public DeviceStructure.Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public DeviceStructure.Builder setMin(Double min) {
            this.min = min;
            return this;
        }

        public DeviceStructure.Builder setMax(Double max) {
            this.max = max;
            return this;
        }

        public DeviceStructure.Builder setDiscrete(Double discrete) {
            this.discrete = discrete;
            return this;
        }

        public DeviceStructure.Builder setAdj(Boolean adj) {
            this.adj = adj;
            return this;
        }

        public DeviceStructure.Builder setRotate(Boolean rotate) {
            this.rotate = rotate;
            return this;
        }

        public DeviceStructure.Builder setSignaling(Boolean signaling) {
            this.signaling = signaling;
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
