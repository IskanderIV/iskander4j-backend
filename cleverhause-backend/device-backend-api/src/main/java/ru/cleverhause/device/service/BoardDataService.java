package ru.cleverhause.device.service;

import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.BoardControlData;
import ru.cleverhause.device.dto.DeviceData;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.device.dto.request.BoardRequestBody;

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



    Board saveData(Long boardUID, BoardRequestBody<DeviceData> dataReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    BoardControlData getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardRequestBody> getLast(int num);
}
