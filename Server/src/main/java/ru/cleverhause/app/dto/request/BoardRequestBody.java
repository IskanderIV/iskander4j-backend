package ru.cleverhause.app.dto.request;

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
    private Error errors;

    public BoardRequestBody(String userName, String pass, Long boardUID, List<T> devices, Error errors) {
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

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }
}
