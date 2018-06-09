package ru.cleverhause.service.arduino;

import ru.cleverhause.rest.board.dto.request.BoardReq;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */

public interface ArduinoDataService {

    boolean checkBoardNumber(BoardReq boardReq);

    boolean save(BoardReq boardReq);

    List<BoardReq> getLast(int num);
}
