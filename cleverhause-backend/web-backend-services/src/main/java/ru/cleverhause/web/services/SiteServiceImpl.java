package ru.cleverhause.web.services;

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
import ru.cleverhause.common.api.service.security.SecurityService;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.BoardControlData;
import ru.cleverhause.common.persist.api.entity.NewBoardUID;
import ru.cleverhause.common.persist.api.entity.User;
import ru.cleverhause.common.persist.api.repository.BoardDao;
import ru.cleverhause.common.persist.api.repository.NewBoardUidDao;
import ru.cleverhause.common.persist.api.repository.UserDao;
import ru.cleverhause.common.utils.JsonUtil;
import ru.cleverhause.device.dto.BoardDto;
import ru.cleverhause.device.dto.DeviceControl;
import ru.cleverhause.device.dto.DeviceData;
import ru.cleverhause.device.dto.DeviceDataRecord;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.web.api.converter.NewBoardUID_To_NewBoardUidFormConverter;
import ru.cleverhause.web.api.dto.request.form.DeviceOnDevicesJspForm;
import ru.cleverhause.web.api.dto.request.form.NewBoardUidForm;
import ru.cleverhause.web.api.dto.request.page.BoardDto_MyBoardsJsp;
import ru.cleverhause.web.api.service.SiteService;
import ru.cleverhause.web.services.model.BoardInfo;
import ru.cleverhause.web.services.model.converter.BoardToBoardInfoConverter;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private NewBoardUidDao newBoardUidDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private NewBoardUID_To_NewBoardUidFormConverter newBoardUidToUidFormConverter;

    @Autowired
    private BoardToBoardInfoConverter boardToBoardInfoConverter;

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

    @Override
    public NewBoardUidForm generateBoardUID() throws Exception {
        User user = userDao.findByUsername(securityService.findLoggedInUsername());
        NewBoardUID savedNewBoardUID = user != null ? user.getNewBoardUID() : null;

        if (savedNewBoardUID != null) {
            return newBoardUidToUidFormConverter.convert(savedNewBoardUID);
        } else if (user != null) {
            NewBoardUID newBoardUID = new NewBoardUID();
            Random rand = new Random();
            int nextUID;
            boolean hasSuchExistedBoardUID;

            do {
                nextUID = rand.nextInt(1000000) + 1000000;
                hasSuchExistedBoardUID = boardDao.findByBoardUID((long) nextUID) != null;
            }
            while (hasSuchExistedBoardUID);

            newBoardUID.setBoardUID((long) nextUID);
            newBoardUID.setBoardName(String.valueOf(nextUID)); //TODO obtain right board name from request
            newBoardUID.setUser(user);
            user.setNewBoardUID(newBoardUID);
            newBoardUidDao.save(newBoardUID);

            return newBoardUidToUidFormConverter.convert(newBoardUID);
        }

        return null;
    }

    // просто вытаскивает всю сохраненную информацию
    @Override
    public List<DeviceOnDevicesJspForm> getDevicesDtoByBoardUID(@Nullable String boardUID) throws IOException {
        List<DeviceOnDevicesJspForm> devices = Lists.newArrayList();
        Board board = findBoardByUID(boardUID);
        BoardInfo boardInfo = boardToBoardInfoConverter.convert(board);

        if (boardInfo != null) {
            for (DeviceStructure deviceStructure : boardInfo.getStructures()) {
                DeviceOnDevicesJspForm device = new DeviceOnDevicesJspForm();

                DeviceControl control = findControlByDeviceId(deviceStructure.getId(), boardInfo.getControls());
                fillControlData(device, control);

                // npe in getDeviceDataList
                List<DeviceDataRecord> dataRecords = boardInfo.getData();
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
        BoardInfo boardInfo = boardToBoardInfoConverter.convert(board);
        boolean controlChanged = false;
        if (boardInfo != null) {
            for (DeviceControl control : boardInfo.getControls()) {
                long savedId = control.getId();
                if (isDeviceAdjustable(savedId, boardInfo)) {
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
                String controlToSave = JsonUtil.toJson(boardInfo.getControls());
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

    private boolean isDeviceAdjustable(final long savedId, final BoardInfo boardInfo) {
        // TODO make BoardService for util operations with board
        for (DeviceStructure deviceStructure : boardInfo.getStructures()) {
            long currDeviceId = deviceStructure.getId();
            if (currDeviceId == savedId) {
                return deviceStructure.getAdj();
            }
        }

        return false;
    }
}