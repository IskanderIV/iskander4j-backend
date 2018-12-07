package ru.cleverhause.app.dto.form;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/10/2018.
 */

@Data
public class NewBoardUidForm implements Serializable {
    private String newBoardUid;
    private String boardName;
}
