package ru.cleverhause.service.board;

import ru.cleverhause.model.Board;
import ru.cleverhause.model.BoardControlData;
import ru.cleverhause.rest.dto.DeviceControl;
import ru.cleverhause.rest.dto.DeviceData;
import ru.cleverhause.rest.dto.DeviceStructure;
import ru.cleverhause.rest.dto.request.BoardRequestBody;
import ru.cleverhause.rest.dto.request.InputBoardControls;
import ru.cleverhause.rest.dto.request.UIRequestBody;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public interface BoardDataService {

    boolean checkBoardNumber(BoardRequestBody boardRequestBody);

    Board registerBoard(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception;

    Long generateBoardUID() throws Exception;

    Board saveData(Long boardUID, BoardRequestBody<DeviceData> dataReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    Board saveControl(Long boardUID, UIRequestBody<InputBoardControls<DeviceControl>> controlReq) throws Exception;

    BoardControlData getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardRequestBody> getLast(int num);
}
