package ru.cleverhause.service.board;

import ru.cleverhause.model.Board;
import ru.cleverhause.rest.dto.DeviceData;
import ru.cleverhause.rest.dto.DeviceStructure;
import ru.cleverhause.rest.dto.request.BoardRequestBody;

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

    Board saveData(Long boardUID, BoardRequestBody<DeviceData> boardSaveReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    Board saveControl(Long boardUID, BoardRequestBody<DeviceData> boardSaveReq) throws Exception;

    Board getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardRequestBody> getLast(int num);
}
