package ru.cleverhause.service.site;

import com.google.common.base.Converter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.cleverhause.api.dto.*;
import ru.cleverhause.api.dto.form.DeviceOnDevicesJspForm;
import ru.cleverhause.api.dto.page.BoardDto_MyBoardsJsp;
import ru.cleverhause.api.service.site.SiteService;
import ru.cleverhause.api.persist.dao.BoardDao;
import ru.cleverhause.api.persist.dao.UserDao;
import ru.cleverhause.api.persist.entities.Board;
import ru.cleverhause.api.persist.entities.BoardControlData;
import ru.cleverhause.api.persist.entities.User;
import ru.cleverhause.util.JsonUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */
@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    private static final int COMMON_SAVED_DATA_NUMBER = 3;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BoardDao boardDao;

    @Autowired
    @Qualifier(value = "boardEntityToBoardDtoConverter")
    private Converter<Board, BoardDto> boardEntityToBoardDtoConverter;

    // сделать более ООПшную разбивку объектов. Над сервисами поставить менеджеров и ввести спец конверторы и тулзы
    @Override
    public List<BoardDto_MyBoardsJsp> getBoardsByUserName(@Nullable String username) {
        String userName = !Strings.isNullOrEmpty(username) ? username : StringUtils.EMPTY;
        User user = userDao.findByUsername(userName);
        List<BoardDto_MyBoardsJsp> userBoards = Lists.newArrayList();
        if (user != null) {
            for (Board board : user.getBoards()) {
                BoardDto_MyBoardsJsp userBoard = new BoardDto_MyBoardsJsp();
                userBoard.setName(board.getBoardName());
                userBoard.setBoardUID(board.getBoardUID());
                userBoards.add(userBoard);
            }
        }
        LOGGER.info("Find boards {}", userBoards);
        return userBoards; // а надо возвращать List<Boards> потом менеджером все собирать и конвертить и выдавать нужное потребителю
        // httpTools или Utils будут работать с RESPONSE
    }

    private Board findBoardByUID(@Nullable String boardUID) {
        Board board = null;
        if (!Strings.isNullOrEmpty(boardUID)) {
            Long uID = Longs.tryParse(boardUID, 10);
            board = uID != null ? boardDao.findByBoardUID(uID) : null;
        }

        return board;
    }

    // просто вытаскивает всю сохраненную информацию
    @Override
    public List<DeviceOnDevicesJspForm> getDevicesDtoByBoardUID(@Nullable String boardUID) throws IOException {
        List<DeviceOnDevicesJspForm> devices = Lists.newArrayList();
        Board board = findBoardByUID(boardUID);
        BoardDto boardDto = boardEntityToBoardDtoConverter.convert(board);

        if (boardDto != null) {
            for (DeviceStructure deviceStructure : boardDto.getStructureList()) {
                DeviceOnDevicesJspForm device = new DeviceOnDevicesJspForm();

                DeviceControl control = findControlByDeviceId(deviceStructure.getId(), boardDto.getControlList());
                fillControlData(device, control);

                // npe in getDeviceDataList
                List<DeviceDataRecord> dataRecords = boardDto.getDataRecords();
                DeviceData data = null;
                if (dataRecords != null && !dataRecords.isEmpty()) {
                    data = findDataByDeviceId(deviceStructure.getId(), findNewestDataRecord(dataRecords).getDeviceDataList());
                }
                fillSavedData(device, data);

                fillStructureData(device, deviceStructure);
                devices.add(device);
            }
        }

        return devices;
    }

    // TODO
    private DeviceDataRecord findNewestDataRecord(List<DeviceDataRecord> boardDataRecords) {
        if (boardDataRecords != null && !boardDataRecords.isEmpty()) {
            boardDataRecords.sort(Comparator.comparing(DeviceDataRecord::getCreated));
            int size = boardDataRecords.size();
            return boardDataRecords.get(size - 1);
        }
        return null;
    }

    private DeviceControl findControlByDeviceId(Long id, List<DeviceControl> deviceControlList) {
        for (DeviceControl c : deviceControlList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }

        return null;
    }

    private DeviceData findDataByDeviceId(Long id, List<DeviceData> deviceDataList) {
        for (DeviceData d : deviceDataList) {
            if (d.getId().equals(id)) { // TODO remake when replace in id in DeviceData on Long id
                return d;
            }
        }
        return null;
    }

    private void fillStructureData(DeviceOnDevicesJspForm device, DeviceStructure deviceStructure) {
        device.setId(deviceStructure.getId());
        device.setDiscrete(deviceStructure.getDiscrete());
        device.setAdj(deviceStructure.getAdj());
        device.setRotate(deviceStructure.getRotate());
        device.setSignaling(deviceStructure.getSignaling());
    }

    private void fillControlData(DeviceOnDevicesJspForm device, DeviceControl control) {
        if (control != null) {
            device.setCtrlVal(control.getCtrlVal());
        } else {
            device.setControlError(true);
        }
    }

    private void fillSavedData(DeviceOnDevicesJspForm device, DeviceData data) {
        if (data != null) {
            device.setAck(data.getAck());
            device.setRadioErr(data.getRadioErr());
        } else {
            device.setDataError(true);
        }
    }

    @Override
    public boolean updateBoardControl(@Nullable String boardUID, final List<DeviceOnDevicesJspForm> dtoDeviceList) throws Exception {
        Board board = findBoardByUID(boardUID);
        BoardDto savedBoardDto = boardEntityToBoardDtoConverter.convert(board);
        boolean controlChanged = false;
        if (savedBoardDto != null) {
            for (DeviceControl control : savedBoardDto.getControlList()) {
                long savedId = control.getId();
                if (isDeviceAdjustable(savedId, savedBoardDto)) {
                    for (DeviceOnDevicesJspForm dtoDevice : dtoDeviceList) {
                        long dtoId = dtoDevice.getId();

                        Double savedCtrlVal = control.getCtrlVal();
                        Double dtoCtrlVal = dtoDevice.getCtrlVal();

                        if (savedId == dtoId && !savedCtrlVal.equals(dtoCtrlVal)) {
                            control.setCtrlVal(dtoDevice.getCtrlVal());
                            controlChanged = true;
                        }
                    }
                }
            }

            // TODO move to updateControl in BoardManager
            if (controlChanged) {
                String controlToSave = JsonUtil.toJson(savedBoardDto.getControlList());
                BoardControlData boardControlData = board.getControlData();
                boardControlData.setData(controlToSave);
                boardControlData.setCreated(new Date()); // TODO перенести в setData
                board.setControlData(boardControlData);
                boardDao.save(board);
                return true;
            }
        }

        return false;
    }

    private boolean isDeviceAdjustable(final long savedId, final BoardDto savedBoardDto) {
        // TODO make BoardService for util operations with board
        for (DeviceStructure deviceStructure : savedBoardDto.getStructureList()) {
            long currDeviceId = deviceStructure.getId();
            if (currDeviceId == savedId) {
                return deviceStructure.getAdj();
            }
        }

        return false;
    }
}