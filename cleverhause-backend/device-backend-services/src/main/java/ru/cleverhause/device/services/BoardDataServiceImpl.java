package ru.cleverhause.device.services;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.cleverhause.common.api.service.security.SecurityService;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.BoardControlData;
import ru.cleverhause.common.persist.api.entity.BoardSavedData;
import ru.cleverhause.common.persist.api.entity.BoardStructure;
import ru.cleverhause.common.persist.api.entity.NewBoardUID;
import ru.cleverhause.common.persist.api.entity.User;
import ru.cleverhause.common.persist.api.entity.utils.EntityUtils;
import ru.cleverhause.common.persist.api.repository.BoardControlDataDao;
import ru.cleverhause.common.persist.api.repository.BoardDao;
import ru.cleverhause.common.persist.api.repository.BoardSavedDataDao;
import ru.cleverhause.common.persist.api.repository.BoardStructureDao;
import ru.cleverhause.common.persist.api.repository.NewBoardUidDao;
import ru.cleverhause.common.persist.api.repository.UserDao;
import ru.cleverhause.common.utils.JsonUtil;
import ru.cleverhause.device.dto.BoardDto;
import ru.cleverhause.device.dto.DeviceControl;
import ru.cleverhause.device.dto.DeviceData;
import ru.cleverhause.device.dto.DeviceDataRecord;
import ru.cleverhause.device.dto.DeviceStructure;
import ru.cleverhause.device.dto.request.BoardRequestBody;
import ru.cleverhause.device.service.BoardDataService;

import java.util.*;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Service
public class BoardDataServiceImpl implements BoardDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoardDataServiceImpl.class);

    private static final int COMMON_SAVED_DATA_NUMBER = 3;

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private BoardControlDataDao boardControlDataDao;

    @Autowired
    private BoardSavedDataDao boardSavedDataDao;

    @Autowired
    private BoardStructureDao boardStructureDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private NewBoardUidDao newBoardUidDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Qualifier(value = "boardEntityToBoardDtoConverter")
    private Converter<Board, BoardDto> boardEntityToBoardDtoConverter;

    @Override
    public boolean checkBoardNumber(Long boardUID, String username) {
        Board savedBoard = findByUID(boardUID);
        if (savedBoard != null && savedBoard.getUser() != null) {
            return username.equals(savedBoard.getUser().getUsername());
        }

        return false;
    }

    @Override
    public Boolean registerBoard(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception {
        Long passedBoardUID = boardRegReq.getBoardUID();
        String passedUsername = boardRegReq.getUsername();
        List<DeviceStructure> passedStructures = boardRegReq.getDevices();
        User user = userDao.findByUsername(passedUsername);
        NewBoardUID savedBoardUidEntity = user.getNewBoardUID();
        boolean isBoardNew = savedBoardUidEntity != null && passedBoardUID.equals(savedBoardUidEntity.getBoardUID());

        if (isBoardNew) {
            // register new board for current user
            Board newBoard = createNewBoard(user, passedBoardUID, savedBoardUidEntity.getBoardName());
            BoardStructure boardStructure = createNewBoardStructure(boardRegReq, newBoard);
            BoardControlData boardControlData = initBoardControlData(boardRegReq, newBoard);

            newBoard.setStructure(boardStructure);
            newBoard.setSavedData(null);
            newBoard.setControlData(boardControlData);
            EntityUtils.addBoardToUser(user, newBoard);

            // TODO make transaction here
            try {
                boardDao.save(newBoard);

                // delete newBoardUidEntity if "save" operation was succeed
                if (boardDao.findByBoardUID(passedBoardUID) != null) {
                    newBoardUidDao.delete(savedBoardUidEntity);
                }

                return true;

            } catch (Exception e) {
                LOGGER.debug("Exception while saving board structure! ", e);

                return false;
            }
        } else {
            // check if current user already has similar one by boardUID, and if true update him in base else ignore
            // update: if structures are identical - refresh structure, else - ? (delete data and refresh control?)
            // now I choose to only refresh control and don't touch data
            boolean isBoardRegistered = false;
            Board registeredBoard = null;
            for (Board board : user.getBoards()) {
                if (passedBoardUID.equals(board.getBoardUID())) {
                    isBoardRegistered = true;
                    registeredBoard = board;
                }
            }

            if (isBoardRegistered) {
                String savedJsonStructure = EntityUtils.getBoardJsonStructure(registeredBoard);
                List<DeviceStructure> savedStructure = Arrays.asList(JsonUtil.fromString(savedJsonStructure, DeviceStructure[].class));
                List<DeviceStructure> passedStructure = boardRegReq.getDevices();
                boolean needRefreshControlData = areStructuresDifferent(savedStructure, passedStructure);
                try {
                    if (needRefreshControlData) {
                        String newControlData = convertRequestToControlData(boardRegReq);
                        registeredBoard.getControlData().setCreated(new Date());
                        registeredBoard.getControlData().setData(newControlData);
                    }
                    boardDao.save(registeredBoard);
                    return true;

                } catch (Exception e) {
                    LOGGER.debug("Exception while updating board structure! ", e);
                    return false;
                }
            }

            return false;
        }
    }

    private Board createNewBoard(User boardOwner, Long boardUID, String boardName) {
        Board board = new Board();
        board.setBoardUID(boardUID);
        board.setBoardName(boardName);
        board.setUser(boardOwner);
        return board;
    }

    private BoardStructure createNewBoardStructure(BoardRequestBody<DeviceStructure> boardRegReq, Board board) throws Exception {
        BoardStructure boardStructure = new BoardStructure();

        String structureJson = JsonUtil.toJson(boardRegReq.getDevices());

        boardStructure.setStructure(structureJson);
        boardStructure.setBoard(board);

        return boardStructure;
    }


    private BoardControlData initBoardControlData(BoardRequestBody<DeviceStructure> boardRegReq, Board board) throws Exception {
        BoardControlData boardControlData = new BoardControlData();
        boardControlData.setCreated(new Date());
        boardControlData.setBoard(board);

        String newControlData = convertRequestToControlData(boardRegReq);
        boardControlData.setData(newControlData);

        return boardControlData;
    }

    private String convertRequestToControlData(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception {
        List<DeviceControl> newControl = Lists.newArrayList();
        List<DeviceStructure> passedStructure = boardRegReq.getDevices();

        for (DeviceStructure structure : passedStructure) {
            DeviceControl control = new DeviceControl();
            control.setId(structure.getId());
            control.setCtrlVal(new Double(structure.getMin())); // TODO replace int with Double in BoardStructure
            newControl.add(control);
        }

        return JsonUtil.toJson(newControl);
    }

    private boolean areStructuresDifferent(List<DeviceStructure> savedStructure, List<DeviceStructure> passedStructure) {
        if (savedStructure.size() == passedStructure.size()) {
            for (int i = 0; i < savedStructure.size(); i++) {
                DeviceStructure s = savedStructure.get(i);
                DeviceStructure p = passedStructure.get(i);
                if (s.getAdj() != p.getAdj()) {
                    return true;
                }
                if (s.getMin() != p.getMin()) {
                    return true;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public Board saveData(Long boardUID, BoardRequestBody<DeviceData> boardSaveReq) throws Exception {
        LOGGER.info("saveData operation");
        List<DeviceData> deviceListToPersist = boardSaveReq.getDevices();
        Board savedBoard = findByUID(boardUID);
        BoardDto savedBoardDto = boardEntityToBoardDtoConverter.convert(savedBoard);

        if (savedBoardDto == null) {
            return null;
        }

        DeviceDataRecord dataRecordBuffer = new DeviceDataRecord();

        // перебираю структуру борда, чтобы по ней фильтровать сохраняемые данные
        for (DeviceStructure deviceStructure : savedBoardDto.getStructureList()) {
            long savedId = deviceStructure.getId();
            DeviceData newDeviceData = new DeviceData();
            newDeviceData.setId(savedId);
            newDeviceData.setAck(deviceStructure.getMin());
            newDeviceData.setAdj(deviceStructure.getAdj());
            newDeviceData.setCtrlVal(deviceStructure.getMin());
            newDeviceData.setRadioErr(true);

            // перебираю сохраняемые данные
            for (DeviceData deviceToPersist : deviceListToPersist) {
                long dtoId = deviceToPersist.getId();
                Double dtoAckVal = deviceToPersist.getAck();

                if (savedId == dtoId) {
                    newDeviceData.setAck(dtoAckVal);
                    newDeviceData.setCtrlVal(deviceToPersist.getCtrlVal());
                    newDeviceData.setAdj(deviceToPersist.getAdj());
                    newDeviceData.setRadioErr(deviceToPersist.getRadioErr());
                }
            }

            dataRecordBuffer.getDeviceDataList().add(newDeviceData);
        }

        // TODO move to updateControl in BoardManager
        String jsonDataToPersist = JsonUtil.toJson(dataRecordBuffer.getDeviceDataList());
        List<BoardSavedData> boardSavedDataEntities = savedBoard.getSavedData();
        BoardSavedData boardSavedData;
        int recordsAmount = savedBoardDto.getDataRecords().size();

        if (recordsAmount == COMMON_SAVED_DATA_NUMBER) {
            Long oldestDataRecordId = findOldestDataRecordId(savedBoardDto.getDataRecords());
            boardSavedData = findBoardSavedDataById(oldestDataRecordId, boardSavedDataEntities);
        } else {
            boardSavedData = new BoardSavedData();
            boardSavedData.setBoard(savedBoard);
            boardSavedDataEntities.add(boardSavedData);
        }

        if (boardSavedData != null) {
            boardSavedData.setData(jsonDataToPersist);
            boardSavedData.setCreated(new Date()); // TODO перенести в setData
            boardSavedDataDao.save(boardSavedData);
        }

        return savedBoard;
    }

    private BoardSavedData findBoardSavedDataById(Long id, List<BoardSavedData> boardSavedDataEntities) {
        for (BoardSavedData boardSavedData : boardSavedDataEntities) {
            if (boardSavedData.getId().equals(id)) {
                return boardSavedData;
            }
        }

        return null;
    }

    // TODO
    private Long findOldestDataRecordId(List<DeviceDataRecord> boardDataRecords) {
        if (boardDataRecords != null && !boardDataRecords.isEmpty()) {
            boardDataRecords.sort(Comparator.comparing(DeviceDataRecord::getCreated));
            return boardDataRecords.get(0).getId();
        }
        return null;
    }

    @Nullable
    private DeviceData getDeviceDataById(final long id, final List<DeviceData> deviceDataList) {
        for (DeviceData deviceData : deviceDataList) {
            if (deviceData.getId() == id) {
                return deviceData;
            }
        }

        return null;
    }

    @Override
    public Board getData(Long boardUID) {
        return null;
    }

    @Override
    public BoardControlData getControl(Board board) {
        return boardControlDataDao.findByBoardId(board.getId());
    }

    @Override
    public Board findByUID(Long boardUID) {
        return boardDao.findByBoardUID(boardUID);
    }

    @Override
    public List<BoardRequestBody> getLast(int num) {
        LOGGER.info("Get operation");
        return null;
    }
}
