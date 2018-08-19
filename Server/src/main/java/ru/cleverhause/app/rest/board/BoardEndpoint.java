package ru.cleverhause.app.rest.board;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceSetting;
import ru.cleverhause.app.dto.DeviceStructure;
import ru.cleverhause.app.dto.request.BoardRequestBody;
import ru.cleverhause.app.dto.response.OutputBoard;
import ru.cleverhause.app.dto.response.ResponseBody;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.service.board.BoardDataService;
import ru.cleverhause.util.JsonUtil;

import javax.servlet.ServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexandr on 15.11.2017.
 */
@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class BoardEndpoint {

    private static final Logger logger = Logger.getLogger(BoardEndpoint.class);

    @Autowired
    private FilterChainProxy filterChainProxy;

    // TODO For test only
    @PostMapping(value = {"/board"})
    public BoardRequestBody<?> boardsPost(ServletRequest req) {
        BoardRequestBody<?> body = null;
        try {
            body = JsonUtil.fromInputStreamToBoardData(req.getInputStream());
        } catch (IOException e) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            logger.info(calendar.getTime() + ": BoardEndpoint. Can't convert request input stream to json"); //TODO
        }
        return body;
    }

    // TODO Test only
    @GetMapping(value = {"/board"})
    public String boardsGet() {
        return "Boards Get";
    }

    @Autowired
    private BoardDataService boardDataService;

    @PostMapping(value = "/board/registration")
    public ResponseBody registerBoard(@RequestBody BoardRequestBody<DeviceStructure> boardRegReq) throws Exception {
        Boolean result = boardDataService.registerBoard(boardRegReq);
        logger.debug("Inside registerBoard");

        return new ResponseBody<>(ObjectUtils.toString(result), null);
    }

    @PostMapping(value = "/board/data")
    public ResponseBody<OutputBoard<DeviceData>> saveBoardData(@RequestBody BoardRequestBody<DeviceData> requestBody) {
        // по-хорошему надо проверить прав юзера, что он зареган и имеет право работать с девайсами
        // алгоритм такой. надо провалидировать. Если есть такой номер, вытащить юзера по номеру борда. Провалидировать креды юзера.
        // Для этого надо хранить привязку к юзеру в таблице. Сохранить данные. Вытащить данные о контролируемых значениях.
        // Составить джесон согласно структуре, сохраненной в базе и пульнуть обратно и вставить туда еще и уникальный номер платы. Плюсом все надо закэшировать
        // создать схемы валидации для разных джесонов. Сложить их в папке. Использовать в фильтрахlogger.info("Inside saveBoardData");
        Long boardUID = requestBody.getBoardUID();
        String message = "OK";
        Board board = null;

        try {
            board = boardDataService.saveData(boardUID, requestBody);
        } catch (Exception e) {
            message = "Error";
        }
        // after that we need to put ctrlVal data into response
        // взять список сохраненных данных, взять список сохраненного управления, скопировать второе в первое по id
        // создать output объект, создать респонз и выдать его
        List<DeviceData> unionDeviceInfo = null;

        ResponseBody<OutputBoard<DeviceData>> response = new ResponseBody<>();
        if (board != null) {
            try {
                List<DeviceControl> deviceControlList = Arrays.asList(JsonUtil.fromString(board.getControlData().getData(), DeviceControl[].class));
                unionDeviceInfo = Arrays.asList(JsonUtil.fromString(board.getSavedData().get(board.getSavedData().size() - 1).getData(), DeviceData[].class));
                for (DeviceData savedData : unionDeviceInfo) {
                    for (DeviceControl control : deviceControlList) {
                        if (savedData.getId().equals(control.getId())) {
                            savedData.setCtrlVal(control.getCtrlVal());
                        }
                    }
                }
            } catch (Exception e) {
                message = "Problem with converting json to Object";
            }
        }

        OutputBoard<DeviceData> output = new OutputBoard<>(unionDeviceInfo, null);
        response.setOutput(output);
        response.setMessage(message);

        return response;
    }

    @GetMapping(value = "/data/last/{num}")
    public ResponseBody<OutputBoard<DeviceSetting>> getLastArduinoData(@PathVariable(name = "num") Integer num) {
        return null;
    }

    @GetMapping(value = "/{username}")
    public ResponseBody<OutputBoard<DeviceSetting>> getBoards(@PathVariable(name = "username") String username) {
        return null;
    }

//    @ExceptionHandler()
//    public ResponseBody handleExceptions(Exception e) {
//        return new ResponseBody("Exceptiion", new Serializable() {
//            @Override
//            public String toString() {
//                return e.getMessage();
//            }
//        });
//    }
}