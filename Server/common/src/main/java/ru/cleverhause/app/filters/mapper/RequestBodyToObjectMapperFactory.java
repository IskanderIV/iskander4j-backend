package ru.cleverhause.app.filters.mapper;

import org.springframework.stereotype.Component;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceStructure;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/14/2018.
 */

@Component
public class RequestBodyToObjectMapperFactory {

    public AbstractRequestBodyToObjectMapper create(Class<? extends Serializable> dtoClass) throws IOException {
        if (dtoClass.isAssignableFrom(DeviceControl.class)) {
            return new BoardControlRequestBodyToObjectMapper();

        } else if (dtoClass.isAssignableFrom(DeviceStructure.class)) {
            return new BoardStructureRequestBodyToObjectMapper();

        } else if (dtoClass.isAssignableFrom(DeviceData.class)) {
            return new BoardDataRequestBodyToObjectMapper();
        }

        return null;
    }
}