package ru.cleverhause.rest.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.cleverhause.model.Board;
import ru.cleverhause.model.User;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.service.arduino.ArduinoDataService;

import java.util.List;

/**
 * Created by Alexandr on 15.11.2017.
 */
@Controller
@RequestMapping(value = "/arduino")
public class ArduinoEndpoint {

    @Autowired
    private ArduinoDataService arduinoDataService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<BoardReq> registerBoard(@RequestBody BoardRegistrationReq arduinoRegJson) {
        //here I have a validated ArduinoJson
        return arduinoDataService.getLast(num);
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    BoardReq saveArduinoData(@RequestBody BoardReq fromBoardReq) throws Exception {
        //here I have validated ArduinoJson
        Board board = arduinoDataService.checkBoardNumber(fromBoardReq);
        if (board != null) {
            arduinoDataService.saveData(board.getId());
            User boardOwner = arduinoDataService.findUserByBoardId(board.getId());
            // по-хорошему надо проверить прав юзера, что он зареган и имеет право работать с девайсами

        }
        if (boardNumber == null || boardNumber.isEmpty()) {
            throw new Exception();
        }
        // алгоритм такой. надо провалидировать. Если есть такой номер, вытащить юзера по номеру борда. Провалидировать креды юзера.
        // Для этого надо хранить привязку к юзеру в таблице. Схранить данные. Вытащить данные о контролируемых значениях.
        // Составить джесон согласно структуре, сохраненной в базе и пульнуть обратно и вставить туда еще и уникальный номер платы. Плюсом все надо закэшировать
        // создать схемы валидации для разных джесонов. Сложить их в папке. Использовать в фильтрах
        Boolean result = arduinoDataService.put(arduinoDataKey, fromBoardReq);
        return;
    }

    @RequestMapping(value = "/data/last/{num}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<BoardReq> getLastArduinoData(@RequestAttribute(name = "num") Integer num) {
        return arduinoDataService.getLast(num);
    }

    @RequestMapping(value = "/boards/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<BoardReq> getBoards(@RequestAttribute(name = "username") String username) {
        return null;
    }

//    @ExceptionHandler
//    public @RequestBody
}