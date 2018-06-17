package ru.cleverhause.service.board;

import ru.cleverhause.model.Board;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.rest.board.dto.structure.DeviceStructure;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public interface BoardDataService {

    boolean checkBoardNumber(BoardReq boardReq);

    Board registerBoard(BoardReq<DeviceStructure> boardRegReq) throws Exception;

    Long generateBoardUID() throws Exception;

    Board saveData(Long boardUID, BoardReq<DeviceData> boardSaveReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    Board saveControl(Long boardUID, BoardReq<DeviceData> boardSaveReq) throws Exception;

    Board getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardReq> getLast(int num);
}
