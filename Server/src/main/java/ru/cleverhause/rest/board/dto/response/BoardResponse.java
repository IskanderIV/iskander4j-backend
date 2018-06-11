package ru.cleverhause.rest.board.dto.response;

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
public class BoardResponse<T extends Serializable> implements Serializable {
    private String message;
    // при ответе в output подставляется соответствующий request
    private T output;

    public BoardResponse(String message, T output) {
        this.message = message;
        this.output = output;
    }

    public BoardResponse() {
    }

    @Override
    public String toString() {
        return "BoardResponse{" +
                "message='" + message + '\'' +
                ", output=" + output +
                '}';
    }
}
