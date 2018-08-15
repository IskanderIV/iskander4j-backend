package ru.cleverhause.service.site;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceStructure;
import ru.cleverhause.app.dto.page.BoardDto_MyBoardsJsp;
import ru.cleverhause.app.dto.page.DeviceDto_DevicesJsp;
import ru.cleverhause.persist.converter.EntitiesToDtoConverter;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.persist.entities.BoardSavedData;
import ru.cleverhause.persist.entities.User;
import ru.cleverhause.service.board.BoardDataService;
import ru.cleverhause.service.user.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardDataService boardDataService;

    @Override
    public List<BoardDto_MyBoardsJsp> getBoardsByUserName(@Nullable String username) {
        User user = userService.findByUserName(!Strings.isNullOrEmpty(username) ? username : StringUtils.EMPTY);
        List<BoardDto_MyBoardsJsp> userBoards = Lists.newArrayList();
        if (user != null) {
            for (Board board : user.getBoards()) {
                BoardDto_MyBoardsJsp userBoard = new BoardDto_MyBoardsJsp();
                userBoard.setName(board.getBoardName());
                userBoard.setBoardUID(board.getBoardUID());
                userBoards.add(userBoard);
            }
        }

        return userBoards;
    }

    @Override
    public List<DeviceDto_DevicesJsp> getDevicesByBoardUID(@Nullable String boardUID) throws IOException {
        List<DeviceDto_DevicesJsp> devices = Lists.newArrayList();

        if (!Strings.isNullOrEmpty(boardUID)) {
            Long uID = Longs.tryParse(boardUID, 10);
            Board board = uID != null ? boardDataService.findByUID(uID) : null;
            if (board != null) {
                List<DeviceStructure> deviceStructureList = EntitiesToDtoConverter.convertBoardStructure(board.getStructure());
                List<DeviceControl> deviceControlList = EntitiesToDtoConverter.convertBoardControlData(board.getControlData());
                List<BoardSavedData> boardSavedDataList = board.getSavedData();
                int lastSaveDataIndex = boardSavedDataList.size() - 1;
                List<DeviceData> deviceDataList = Lists.newArrayList();
                if (lastSaveDataIndex >= 0) {
                    deviceDataList = EntitiesToDtoConverter.convertBoardSavedData(boardSavedDataList.get(lastSaveDataIndex));
                }

                for (DeviceStructure deviceStructure : deviceStructureList) {
                    DeviceDto_DevicesJsp device = new DeviceDto_DevicesJsp();

                    DeviceControl control = findControlByDeviceId(deviceStructure.getId(), deviceControlList);
                    fillControlData(device, control);

                    DeviceData data = findDataByDeviceId(deviceStructure.getId(), deviceDataList);
                    fillSavedData(device, data);

                    fillStructureData(device, deviceStructure);
                    devices.add(device);
                }
            }
        }

        return devices;
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
            if (new Long(d.getId()).equals(id)) { // TODO remake when replace in id in DeviceData on Long id
                return d;
            }
        }

        return null;
    }

    private void fillStructureData(DeviceDto_DevicesJsp device, DeviceStructure deviceStructure) {
        device.setId(deviceStructure.getId());
        device.setDiscrete(deviceStructure.getDiscrete());
        device.setAdj(deviceStructure.getAdj());
        device.setRotate(deviceStructure.getRotate());
        device.setSignaling(deviceStructure.getSignaling());
    }

    private void fillControlData(DeviceDto_DevicesJsp device, DeviceControl control) {
        if (control != null) {
            device.setCtrlVal(control.getCtrlVal());
        } else {
            device.setControlError(true);
        }
    }

    private void fillSavedData(DeviceDto_DevicesJsp device, DeviceData data) {
        if (data != null) {
            device.setAck(data.getAck());
            device.setRadioErr(data.getRadioErr());
        } else {
            device.setDataError(true);
        }
    }
}