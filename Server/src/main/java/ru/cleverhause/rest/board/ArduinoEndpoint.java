package ru.cleverhause.rest.board;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cleverhause.model.Board;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.rest.board.dto.response.BoardResponse;
import ru.cleverhause.rest.frontend.dto.response.BoardUID;
import ru.cleverhause.service.board.BoardDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr on 15.11.2017.
 */
@RestController
@RequestMapping(value = "/boards",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class ArduinoEndpoint {

    private static final Logger logger = Logger.getLogger(ArduinoEndpoint.class);

    @Autowired
    private BoardDataService boardDataService;

    @PostMapping(value = "/board/registration")
    public BoardResponse<BoardReq<DeviceSetting>> registerBoard(@RequestBody BoardReq<DeviceSetting> boardRegReq) {
        //here I have a validated ArduinoJson
        // записываем в boardStructure соотв данные о структуре. По-хорошему надо тащить информацию
        // о бордах в профиль, чтобы не лазить в базу. А ее обновлять в определенных случаях

        try {
            boardDataService.saveBoardStructure(boardRegReq.getBoardUID(), boardRegReq.getDevices());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("Inside registerBoard");
        return new BoardResponse<>("Hello", boardRegReq);
    }

    @PostMapping(value = "/board/data")
    public BoardResponse<BoardReq<DeviceData>> saveBoardData(@RequestBody BoardReq<DeviceData> fromBoardReq) throws Exception {
        //here I have validated ArduinoJson
        logger.info("Inside saveBoardData");
        String boardUID = fromBoardReq.getBoardUID();
        String username = fromBoardReq.getUsername();
        String password = fromBoardReq.getPassword();

        Board board = boardDataService.saveData(boardUID, fromBoardReq);
        if (board != null) {
            board = boardDataService.getControl(board);
        }

//        if (board != null) {
//            boardDataService.saveData(board.getId());
//            User boardOwner = boardDataService.findUserByBoardId(board.getId());
            // по-хорошему надо проверить прав юзера, что он зареган и имеет право работать с девайсами

//        }
//        if (boardNumber == null || boardNumber.isEmpty()) {
////            throw new Exception();
////        }
        // алгоритм такой. надо провалидировать. Если есть такой номер, вытащить юзера по номеру борда. Провалидировать креды юзера.
        // Для этого надо хранить привязку к юзеру в таблице. Сохранить данные. Вытащить данные о контролируемых значениях.
        // Составить джесон согласно структуре, сохраненной в базе и пульнуть обратно и вставить туда еще и уникальный номер платы. Плюсом все надо закэшировать
        // создать схемы валидации для разных джесонов. Сложить их в папке. Использовать в фильтрах
//        Boolean result = boardDataService.put(arduinoDataKey, fromBoardReq);
        List<DeviceData> responseDevInfo = new ArrayList<>();
        for (DeviceData deviceData : board.getControlData().getData()) {

        }
        BoardReq<DeviceData> response = new BoardReq<>(username, password, boardUID, responseDevInfo);
        return new BoardResponse<>("Hello", response);
    }

    @GetMapping(value = "/data/last/{num}")
    public
    BoardResponse<BoardReq<DeviceSetting>> getLastArduinoData(@RequestAttribute(name = "num") Integer num) {
        return null;
    }

    @GetMapping(value = "/{username}")
    public
    BoardResponse<BoardReq<DeviceSetting>> getBoards(@RequestAttribute(name = "username") String username) {
        return null;
    }

    @GetMapping(value = "/board/uid")
    public
    BoardResponse<BoardUID> getBoardUID() {
        logger.info("Inside getBoardUID");
        return new BoardResponse<>("Hello", new BoardUID("123456"));
    }

//    @ExceptionHandler
//    public BoardResponse handleExceptions(Exception e) {
//        return new BoardResponse("Exceptiion", new Serializable() {
//            @Override
//            public String toString() {
//                return e.getMessage();
//            }
//        });
//    }
}