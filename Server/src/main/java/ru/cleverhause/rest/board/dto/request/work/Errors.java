package ru.cleverhause.rest.board.dto.request.work;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Alexandr on 04.01.2018.
 */
@Getter
@Setter
public class Errors implements Serializable {
    private Boolean gsm;
    private Boolean lcd;
    private Boolean radio;

    public Errors(Boolean gsm, Boolean lcd, Boolean radio) {
        this.gsm = gsm;
        this.lcd = lcd;
        this.radio = radio;
    }

    public Errors() {
    }

    @Override
    public String toString() {
        return "Errors{" +
                "gsm=" + gsm +
                ", lcd=" + lcd +
                ", radio=" + radio +
                '}';
    }
}
