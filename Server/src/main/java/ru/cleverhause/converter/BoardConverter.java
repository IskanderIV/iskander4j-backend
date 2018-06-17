package ru.cleverhause.converter;

import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.util.JsonUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class BoardConverter {
    /**
     * Save data operations converters
     */
    // можно сделать конвертер, кот будет совершать эту операцию на входе контроллера
    public static <T extends Serializable> String convertDeviceDataListToJson(List<T> deviceDataList) throws Exception {
        StringBuilder reqJson = new StringBuilder();
        for (T deviceData : deviceDataList) {
            reqJson.append(JsonUtil.toJson(deviceData));
        }

        return reqJson.toString();
    }

//    public static <T extends Serializable> List<T> convertJsonToDeviceDataList(String deviceDataJson) throws Exception {
//        return Arrays.asList(JsonUtil.fromString(deviceDataJson, T[].class));
//    }

    /**
     * Structure's converters
     */

}
