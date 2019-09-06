package ru.cleverhause.web.services.model;

import com.google.common.base.Objects;
import ru.cleverhause.device.dto.DeviceControl;
import ru.cleverhause.device.dto.DeviceDataRecord;
import ru.cleverhause.device.dto.DeviceStructure;

import java.util.List;

public class BoardInfo {
    private final List<DeviceControl> controls;
    private final List<DeviceStructure> structures;
    private final List<DeviceDataRecord> data;

    public BoardInfo(List<DeviceControl> controls, List<DeviceStructure> structures, List<DeviceDataRecord> data) {
        this.controls = controls;
        this.structures = structures;
        this.data = data;
    }

    public List<DeviceControl> getControls() {
        return controls;
    }

    public List<DeviceStructure> getStructures() {
        return structures;
    }

    public List<DeviceDataRecord> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardInfo boardInfo = (BoardInfo) o;
        return Objects.equal(controls, boardInfo.controls) &&
                Objects.equal(structures, boardInfo.structures) &&
                Objects.equal(data, boardInfo.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(controls, structures, data);
    }

    @Override
    public String toString() {
        return "BoardInfo{" +
                "controls=" + controls +
                ", structures=" + structures +
                ", data=" + data +
                '}';
    }
}
