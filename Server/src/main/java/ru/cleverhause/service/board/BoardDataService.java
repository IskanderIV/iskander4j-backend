package ru.cleverhause.service.board;

import ru.cleverhause.model.Board;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public interface BoardDataService {

    boolean checkBoardNumber(BoardReq boardReq);

    Board saveBoardStructure(String boardUID, List<DeviceSetting> boardReqReq) throws Exception;

    Board saveData(String boardUID, BoardReq<DeviceData> boardSaveReq) throws Exception;

    Board getData(String boardUID) throws Exception;

    Board saveControl(String boardUID, BoardReq<DeviceData> boardSaveReq) throws Exception;

    Board getControl(Board board) throws Exception;

    Board findByUID(String boardUID);

    List<BoardReq> getLast(int num);
}
