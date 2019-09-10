package ru.cleverhause.web.api.dto.request.form;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/10/2018.
 */

public class NewBoardUidForm implements Serializable {
    private String newBoardUid;
    private String boardName;

    public NewBoardUidForm(String newBoardUid, String boardName) {
        this.newBoardUid = newBoardUid;
        this.boardName = boardName;
    }

    public NewBoardUidForm() {
    }

    public String getNewBoardUid() {
        return newBoardUid;
    }

    public void setNewBoardUid(String newBoardUid) {
        this.newBoardUid = newBoardUid;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
