package ru.cleverhause.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Alexandr on 04.01.2018.
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeviceErrors implements Serializable {
    private Boolean radioError;

    @Override
    public String toString() {
        return "DeviceErrors{" +
                "radioError=" + radioError +
                '}';
    }
}
