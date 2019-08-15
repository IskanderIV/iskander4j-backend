package ru.cleverhause.api.service.board;

import ru.cleverhause.api.dto.DeviceData;
import ru.cleverhause.api.dto.DeviceStructure;
import ru.cleverhause.api.dto.form.NewBoardUidForm;
import ru.cleverhause.api.dto.request.BoardRequestBody;
import ru.cleverhause.api.persist.entities.Board;
import ru.cleverhause.api.persist.entities.BoardControlData;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public interface BoardDataService {

    boolean checkBoardNumber(Long boardUID, String username);

    Boolean registerBoard(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception;

    NewBoardUidForm generateBoardUID() throws Exception;

    Board saveData(Long boardUID, BoardRequestBody<DeviceData> dataReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    BoardControlData getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardRequestBody> getLast(int num);
}
