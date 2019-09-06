package ru.cleverhause.web.services.model.converter;

import com.google.common.base.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.BoardSavedData;
import ru.cleverhause.common.utils.JsonUtil;
import ru.cleverhause.device.dto.DeviceControl;
import ru.cleverhause.device.dto.DeviceDataRecord;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.web.services.model.BoardInfo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ParametersAreNonnullByDefault
public class BoardToBoardInfoConverter extends Converter<Board, BoardInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardToBoardInfoConverter.class);

    @Override
    protected BoardInfo doForward(Board board) {
        return Optional.of(board)
                .map(b -> {

                    try {
                        List<DeviceControl> controls;
                        List<DeviceStructure> structures;
                        List<DeviceDataRecord> data;
                        controls = Arrays.asList(JsonUtil.fromString(b.getControlData().getData(), DeviceControl[].class));
                        structures = Arrays.asList(JsonUtil.fromString(b.getStructure().getStructure(), DeviceStructure[].class));
                        data = getBoardSavedData(b).stream()
                                .map(savedData -> {
                                    try {
                                        return JsonUtil.fromString(savedData, DeviceDataRecord.class);
                                    } catch (Exception e) {
                                        LOGGER.error("Can't convert board data {} to DeviceDataRecord", b);
                                        throw new RuntimeException("Can't convert board data to DeviceDataRecord");
                                    }
                                }).collect(Collectors.toList());
                        return new BoardInfo(controls, structures, data);
                    } catch (Exception e) {
                        LOGGER.error("Can't convert board data {}", b);
                        throw new RuntimeException("Can't convert board data");
                    }
                }).orElse(null);
    }

    @Override
    protected Board doBackward(BoardInfo boardInfo) {
        return null;
    }

    private List<String> getBoardSavedData(Board board) {
        return Optional.of(board)
                .map(Board::getSavedData)
                .orElse(new ArrayList<>())
                .stream()
                .map(BoardSavedData::getData)
                .collect(Collectors.toList());
    }
}
