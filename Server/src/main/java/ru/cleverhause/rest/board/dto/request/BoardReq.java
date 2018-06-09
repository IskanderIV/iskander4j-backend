package ru.cleverhause.rest.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardReq implements Serializable {
    private List<DeviceInfo> devices = new ArrayList<DeviceInfo>();

    public static class Builder {
        private List<DeviceInfo> devices;

    }
}
