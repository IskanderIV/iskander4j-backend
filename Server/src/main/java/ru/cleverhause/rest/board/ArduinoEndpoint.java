package ru.cleverhause.rest.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.cleverhause.model.Board;
import ru.cleverhause.model.User;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.response.BoardResponse;
import ru.cleverhause.rest.board.dto.response.ui.BoardUID;
import ru.cleverhause.service.arduino.ArduinoDataService;

import java.util.List;

/**
 * Created by Alexandr on 15.11.2017.
 */
@RestController
@RequestMapping(value = "/boards",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class ArduinoEndpoint {

    @Autowired
    private ArduinoDataService arduinoDataService;

    @PostMapping(value = "/board/registration")
    public BoardResponse<BoardReq<DeviceSetting>> registerBoard(@RequestBody BoardReq<DeviceSetting> arduinoRegJson) {
        //here I have a validated ArduinoJson

        return null;
    }

    @PostMapping(value = "/data")
    public BoardReq saveArduinoData(@RequestBody BoardReq fromBoardReq) throws Exception {
        //here I have validated ArduinoJson
//        Board board = arduinoDataService.checkBoardNumber(fromBoardReq);
//        if (board != null) {
//            arduinoDataService.saveData(board.getId());
//            User boardOwner = arduinoDataService.findUserByBoardId(board.getId());
            // по-хорошему надо проверить прав юзера, что он зареган и имеет право работать с девайсами

//        }
//        if (boardNumber == null || boardNumber.isEmpty()) {
////            throw new Exception();
////        }
        // алгоритм такой. надо провалидировать. Если есть такой номер, вытащить юзера по номеру борда. Провалидировать креды юзера.
        // Для этого надо хранить привязку к юзеру в таблице. Схранить данные. Вытащить данные о контролируемых значениях.
        // Составить джесон согласно структуре, сохраненной в базе и пульнуть обратно и вставить туда еще и уникальный номер платы. Плюсом все надо закэшировать
        // создать схемы валидации для разных джесонов. Сложить их в папке. Использовать в фильтрах
//        Boolean result = arduinoDataService.put(arduinoDataKey, fromBoardReq);
        return null;
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
        return null;
    }
//    @ExceptionHandler
//    public @RequestBody
}