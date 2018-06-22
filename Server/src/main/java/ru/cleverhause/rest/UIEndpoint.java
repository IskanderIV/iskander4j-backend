package ru.cleverhause.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.model.Board;
import ru.cleverhause.rest.dto.DeviceControl;
import ru.cleverhause.rest.dto.request.InputBoardControls;
import ru.cleverhause.rest.dto.request.UIRequestBody;
import ru.cleverhause.rest.dto.response.ResponseBody;
import ru.cleverhause.rest.dto.response.ui.OutputDevices;
import ru.cleverhause.service.board.BoardDataService;
import ru.cleverhause.util.JsonUtil;

import java.util.Arrays;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/22/2018.
 */
@RestController
@RequestMapping(value = "/boards",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class UIEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(UIEndpoint.class);

    @Autowired
    private BoardDataService boardDataService;

    @PostMapping(value = "/control")
    public ResponseBody<OutputDevices<DeviceControl>> saveBoardControl(@RequestBody UIRequestBody<InputBoardControls<ru.cleverhause.rest.dto.DeviceControl>> requestBody) {
        Long boardUID = requestBody.getBoardUID();
        String message = "";
        Board board = null;

        try {
            board = boardDataService.saveControl(boardUID, requestBody);
        } catch (Exception e) {
            message = "Can't save data";
        }

        ResponseBody<OutputDevices<DeviceControl>> response = new ResponseBody<>();
        OutputDevices<DeviceControl> output = new OutputDevices<>();
        if (board != null) {
            try {
                output.setDevices(Arrays.asList(JsonUtil.fromString(board.getControlData().getData(), DeviceControl[].class)));
            } catch (Exception e) {
                message = "Can't convert from string to Object while saveBoardControl";
            }
        }

        response.setOutput(output);
        response.setMessage(message);

        return response;
    }
}
