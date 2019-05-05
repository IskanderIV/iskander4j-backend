package ru.cleverhause.api.dto;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */

public class DeviceStructure implements Serializable {
    private Long id;
    private Double min;
    private Double max;
    private Double discrete;
    private Boolean adj;
    private Boolean rotate;
    private Boolean signaling;

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

    public DeviceStructure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getDiscrete() {
        return discrete;
    }

    public void setDiscrete(Double discrete) {
        this.discrete = discrete;
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

    public static class Builder {
        private Long id;
        private Double min;
        private Double max;
        private Double discrete;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        public Builder(Long id, Double min, Double max, Double discrete, Boolean adj, Boolean rotate, Boolean signaling) {
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
