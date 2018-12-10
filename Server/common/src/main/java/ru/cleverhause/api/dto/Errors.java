package ru.cleverhause.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Alexandr on 04.01.2018.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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

    public Boolean getGsm() {
        return gsm;
    }

    public void setGsm(Boolean gsm) {
        this.gsm = gsm;
    }

    public Boolean getLcd() {
        return lcd;
    }

    public void setLcd(Boolean lcd) {
        this.lcd = lcd;
    }

    public Boolean getRadio() {
        return radio;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
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
