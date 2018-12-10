package ru.cleverhause.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */
@Getter
@Setter
public class ResponseBody<T extends Serializable> implements Serializable {
    private String message;
    // при ответе в output подставляется соответствующий request
    private T output;

    public ResponseBody(String message, T output) {
        this.message = message;
        this.output = output;
    }

    public ResponseBody() {
    }

    @Override
    public String toString() {
        return "BoardResponseBody{" +
                "message='" + message + '\'' +
                ", output=" + output +
                '}';
    }
}
