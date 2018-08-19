package ru.cleverhause.service.board.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.cleverhause.app.dto.BoardDto;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceStructure;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.persist.entities.BoardControlData;
import ru.cleverhause.persist.entities.BoardSavedData;
import ru.cleverhause.persist.entities.BoardStructure;

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
            int lastSaveDataIndex = boardSavedDataList.size() - 1;
            List<DeviceData> deviceDataList = Lists.newArrayList();
            if (lastSaveDataIndex >= 0) {
                deviceDataList = convertBoardSavedData(boardSavedDataList.get(lastSaveDataIndex));
            }
            boardDto.setDataList(deviceDataList);
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

    private List<DeviceData> convertBoardSavedData(BoardSavedData boardSavedData) {
        List<DeviceData> deviceDataList = Lists.newArrayList();

        if (boardSavedData != null) {
            try {
                String data = boardSavedData.getData();
                deviceDataList = Arrays.asList(MAPPER.readValue(data, DeviceData[].class));
            } catch (IOException e) {
                LOGGER.info("Can't convert DeviceData List", e.getStackTrace()[0]);
            }
        }

        return deviceDataList;
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