package ru.cleverhause.app.dto.request;

import ru.cleverhause.app.dto.Errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/1/2017.
 */
public class BoardRequestBody<T extends Serializable> extends AbstractRequestBody implements Serializable {
    private List<T> devices = new ArrayList<>();
    private Errors errors;

    public BoardRequestBody(String userName, String pass, Long boardUID, List<T> devices, Errors errors) {
        super(userName, pass, boardUID);
        this.devices = devices;
        this.errors = errors;
    }

    public BoardRequestBody() {
        super();
    }

    public List<T> getDevices() {
        return devices;
    }

    public void setDevices(List<T> devices) {
        this.devices = devices;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
