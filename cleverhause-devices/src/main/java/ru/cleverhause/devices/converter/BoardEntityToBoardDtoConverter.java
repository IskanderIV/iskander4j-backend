package ru.cleverhause.devices.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.BoardControlData;
import ru.cleverhause.common.persist.api.entity.BoardSavedData;
import ru.cleverhause.common.persist.api.entity.BoardStructure;
import ru.cleverhause.device.api.dto.BoardDto;
import ru.cleverhause.device.api.dto.DeviceControl;
import ru.cleverhause.device.api.dto.DeviceData;
import ru.cleverhause.device.api.dto.DeviceDataRecord;
import ru.cleverhause.device.api.dto.DeviceStructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class BoardEntityToBoardDtoConverter extends Converter<Board, BoardDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardEntityToBoardDtoConverter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    protected BoardDto doForward(@Nullable Board board) {
        BoardDto boardDto = new BoardDto();

        if (board != null) {
            boardDto.setStructureList(convertBoardStructure(board.getStructure()));
            boardDto.setControlList(convertBoardControlData(board.getControlData()));

            List<BoardSavedData> boardSavedDataList = board.getSavedData();
            int numOfSavedDataRows = boardSavedDataList.size();
            List<DeviceDataRecord> deviceDataRecord = convertBoardSavedData(boardSavedDataList);
            boardDto.setDataRecords(deviceDataRecord);
        }

        return boardDto;
    }

    @Override
    protected Board doBackward(BoardDto boardDto) {
        return null;
    }

    private List<DeviceStructure> convertBoardStructure(BoardStructure boardStructure) {
        List<DeviceStructure> deviceStructureList = Lists.newArrayList();

        if (boardStructure != null) {
            try {
                String structure = boardStructure.getStructure();
                deviceStructureList = Arrays.asList(MAPPER.readValue(structure, DeviceStructure[].class));
            } catch (IOException e) {
                LOGGER.info("Can't convert DeviceStructure List", e.getStackTrace()[0]);
            }
        }

        return deviceStructureList;
    }

    private List<DeviceDataRecord> convertBoardSavedData(List<BoardSavedData> boardDataEntries) {
        List<DeviceDataRecord> boardDataList = Lists.newArrayList();

        if (boardDataEntries != null) {
            for (BoardSavedData entry : boardDataEntries) {
                String entryData = entry.getData();
                try {
                    List<DeviceData> deviceDataList = Arrays.asList(MAPPER.readValue(entryData, DeviceData[].class));
                    DeviceDataRecord deviceDataRecord = new DeviceDataRecord();
                    deviceDataRecord.setDeviceDataList(deviceDataList);
                    deviceDataRecord.setId(entry.getId());
                    deviceDataRecord.setCreated(entry.getCreated());
                    boardDataList.add(deviceDataRecord);

                } catch (IOException e) {
                    LOGGER.info("Can't convert DeviceData List", e.getStackTrace()[0]);
                }
            }

        }

        return boardDataList;
    }

    private List<DeviceControl> convertBoardControlData(BoardControlData boardControlData) {
        List<DeviceControl> deviceControlList = Lists.newArrayList();

        if (boardControlData != null) {
            try {
                String data = boardControlData.getData();
                deviceControlList = Arrays.asList(MAPPER.readValue(data, DeviceControl[].class));
            } catch (IOException e) {
                LOGGER.info("Can't convert DeviceControl List", e.getStackTrace()[0]);
            }
        }

        return deviceControlList;
    }
}