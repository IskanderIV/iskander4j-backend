package ru.cleverhause.devices.service;

import java.util.List;

public interface BoardDataService {

    boolean checkBoardNumber(Long boardUID, String username);

    Boolean registerBoard(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception;

    Board saveData(Long boardUID, BoardRequestBody<DeviceData> dataReq) throws Exception;

    Board getData(Long boardUID) throws Exception;

    BoardControlData getControl(Board board) throws Exception;

    Board findByUID(Long boardUID);

    List<BoardRequestBody> getLast(int num);
}
