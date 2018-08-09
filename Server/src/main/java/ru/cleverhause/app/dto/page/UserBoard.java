package ru.cleverhause.app.dto.page;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */

@Data
public class UserBoard implements Serializable {
    private String name;
    private String numOfDevices;
}
