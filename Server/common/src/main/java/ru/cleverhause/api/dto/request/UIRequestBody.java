package ru.cleverhause.api.dto.request;

import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
public class UIRequestBody<T extends Serializable> extends AbstractRequestBody implements Serializable {
    private T input;

    public UIRequestBody(String userName, String pass, Long boardUID, T devices) {
        super(userName, pass, boardUID);
        this.input = devices;
    }

    public UIRequestBody() {
    }

    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }
}
