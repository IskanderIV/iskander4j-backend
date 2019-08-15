package ru.cleverhause.filters.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.cleverhause.api.dto.DeviceStructure;
import ru.cleverhause.api.dto.request.BoardRequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/13/2018.
 */

public class BoardStructureRequestBodyToObjectMapper extends AbstractRequestBodyToObjectMapper<DeviceStructure> {

    @Override
    public BoardRequestBody<DeviceStructure> map(HttpServletRequest request) throws IOException {
        return MAPPER.readValue(request.getInputStream(), new TypeReference<BoardRequestBody<DeviceStructure>>() {
        });
    }
}