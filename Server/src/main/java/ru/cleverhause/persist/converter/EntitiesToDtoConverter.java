package ru.cleverhause.persist.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceStructure;
import ru.cleverhause.persist.entities.BoardControlData;
import ru.cleverhause.persist.entities.BoardSavedData;
import ru.cleverhause.persist.entities.BoardStructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EntitiesToDtoConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<DeviceStructure> convertBoardStructure(BoardStructure boardStructure) throws IOException {
        List<DeviceStructure> deviceStructureList = Lists.newArrayList();

        if (boardStructure != null) {
            String structure = boardStructure.getStructure();
            deviceStructureList = Arrays.asList(MAPPER.readValue(structure, DeviceStructure[].class));
        }

        return deviceStructureList;
    }

    public static List<DeviceData> convertBoardSavedData(BoardSavedData boardSavedData) throws IOException {
        List<DeviceData> deviceDataList = Lists.newArrayList();

        if (boardSavedData != null) {
            String data = boardSavedData.getData();
            deviceDataList = Arrays.asList(MAPPER.readValue(data, DeviceData[].class));
        }

        return deviceDataList;
    }

    public static List<DeviceControl> convertBoardControlData(BoardControlData boardControlData) throws IOException {
        List<DeviceControl> deviceControlList = Lists.newArrayList();

        if (boardControlData != null) {
            String data = boardControlData.getData();
            deviceControlList = Arrays.asList(MAPPER.readValue(data, DeviceControl[].class));
        }

        return deviceControlList;
    }
}