package ru.cleverhause.api.dto.form;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/15/2018.
 */

@Data
@Getter
public class Device_DevicesJspForm implements Serializable {
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
}
