package ru.cleverhause.api.dto.response;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/9/2018.
 */

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getOutput() {
        return output;
    }

    public void setOutput(T output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "BoardResponseBody{" +
                "message='" + message + '\'' +
                ", output=" + output +
                '}';
    }
}
