package ru.cleverhause.device.api.rest;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.utils.JsonUtil;
import ru.cleverhause.device.api.dto.DeviceControl;
import ru.cleverhause.device.api.dto.DeviceData;
import ru.cleverhause.device.api.dto.DeviceSetting;
import ru.cleverhause.device.api.dto.DeviceStructure;
import ru.cleverhause.device.api.dto.request.BoardRequestBody;
import ru.cleverhause.device.api.dto.response.OutputBoard;
import ru.cleverhause.device.api.dto.response.ResponseBody;
import ru.cleverhause.device.api.service.BoardDataService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexandr on 15.11.2017.
 */
@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class BoardEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardEndpoint.class);

    @GetMapping(value = {"/board/healthcheck"})
    public String boardHealthCheck() {
        LOGGER.info("Inside Health Check!");
        return "Boards Get";
    }

    @Autowired
    private BoardDataService boardDataService;

    @PostMapping(value = "/board/registration")
    public ResponseBody registerBoard(@RequestBody BoardRequestBody<DeviceStructure> boardRegReq) throws Exception {
        Boolean result = boardDataService.registerBoard(boardRegReq);
        LOGGER.debug("Inside registerBoard");

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