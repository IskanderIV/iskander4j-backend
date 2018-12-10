package ru.cleverhause.api.dto.page;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */

@Data
public class BoardDto_MyBoardsJsp implements Serializable {
    private Long boardUID;
    private String name;
    private String numOfDevices;
}
