package ru.cleverhause.rest.board;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.cleverhause.model.Board;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.rest.board.dto.response.BoardResponse;
import ru.cleverhause.rest.board.dto.structure.DeviceStructure;
import ru.cleverhause.rest.frontend.dto.request.DeviceControl;
import ru.cleverhause.rest.frontend.dto.request.UIRequest;
import ru.cleverhause.rest.frontend.dto.response.BoardUID;
import ru.cleverhause.rest.frontend.dto.response.UIResponseBody;
import ru.cleverhause.service.board.BoardDataService;
import ru.cleverhause.util.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
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
    public BoardResponse registerBoard(@RequestBody BoardReq<DeviceStructure> boardRegReq) throws Exception {
        boardDataService.registerBoard(boardRegReq);

        logger.debug("Inside registerBoard");
        return new BoardResponse<>("Hello", null);
    }

    @PostMapping(value = "/board/data")
    public BoardResponse<BoardReq<DeviceData>> saveBoardData(@RequestBody BoardReq<DeviceData> fromBoardReq, HttpServletResponse res) throws Exception {
        //here I have validated ArduinoJson
        logger.info("Inside saveBoardData");
        Long boardUID = fromBoardReq.getBoardUID();
        String username = fromBoardReq.getUsername();
        String password = fromBoardReq.getPassword();

        Board board = boardDataService.saveData(boardUID, fromBoardReq);
        List<DeviceData> responseDevInfo = new ArrayList<>();
        if (board != null) {
            List<DeviceControl> deviceControlList = Arrays.asList(JsonUtil.fromString(board.getControlData().getData(), DeviceControl[].class));
            List<DeviceData> deviceDataList = Arrays.asList(JsonUtil.fromString(board.getSavedData().get(0).getData(), DeviceData[].class));
            responseDevInfo = new ArrayList<>(deviceDataList);
            for (DeviceData deviceData : responseDevInfo) {
                for (DeviceControl newDevCntrl : deviceControlList) {
                    if (deviceData.getId() == newDevCntrl.getId()) {
                        deviceData.setCtrlVal(newDevCntrl.getControl());
                    }
                }
            }
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

        res.setStatus(HttpStatus.OK.value());
        BoardReq<DeviceData> response = new BoardReq<>(username, password, boardUID, responseDevInfo);
        return new BoardResponse<>("Hello", response);
    }

    @GetMapping(value = "/data/last/{num}")
    public BoardResponse<BoardReq<DeviceSetting>> getLastArduinoData(@RequestAttribute(name = "num") Integer num) {
        return null;
    }

    @GetMapping(value = "/{username}")
    public BoardResponse<BoardReq<DeviceSetting>> getBoards(@RequestAttribute(name = "username") String username) {
        return null;
    }

    @GetMapping(value = "/board/uid")
    public UIResponseBody<BoardUID> getBoardUID(@RequestBody UIRequest uiRequest) throws Exception {
        logger.info("Inside getBoardUID");
        Long newBoardUID = boardDataService.generateBoardUID();

        return new UIResponseBody<>("Hello", new BoardUID(newBoardUID.toString()));
    }

//    @ExceptionHandler()
//    public BoardResponse handleExceptions(Exception e) {
//        return new BoardResponse("Exceptiion", new Serializable() {
//            @Override
//            public String toString() {
//                return e.getMessage();
//            }
//        });
//    }
}