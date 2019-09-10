package ru.cleverhause.device.api.filters.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.cleverhause.device.api.dto.DeviceData;
import ru.cleverhause.device.api.dto.request.BoardRequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/13/2018.
 */

public class BoardDataRequestBodyToObjectMapper extends AbstractRequestBodyToObjectMapper<DeviceData> {

    @Override
    public BoardRequestBody<DeviceData> map(HttpServletRequest request) throws IOException {
        return MAPPER.readValue(request.getInputStream(), new TypeReference<BoardRequestBody<DeviceData>>() {
        });
    }
}