package ru.cleverhause.app.filters.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.cleverhause.app.dto.request.BoardRequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/13/2018.
 */

public abstract class AbstractRequestBodyToObjectMapper<T extends Serializable> {
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    public abstract BoardRequestBody<T> map(HttpServletRequest request) throws IOException;
}