package ru.cleverhause.service.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.dao.BoardControlDataDao;
import ru.cleverhause.dao.BoardDao;
import ru.cleverhause.dao.BoardSavedDataDao;
import ru.cleverhause.dao.BoardStructureDao;
import ru.cleverhause.dao.UserDao;
import ru.cleverhause.model.Board;
import ru.cleverhause.model.BoardControlData;
import ru.cleverhause.model.BoardSavedData;
import ru.cleverhause.model.BoardStructure;
import ru.cleverhause.model.User;
import ru.cleverhause.rest.dto.DeviceControl;
import ru.cleverhause.rest.dto.DeviceData;
import ru.cleverhause.rest.dto.DeviceStructure;
import ru.cleverhause.rest.dto.request.BoardRequestBody;
import ru.cleverhause.rest.dto.request.InputBoardControls;
import ru.cleverhause.rest.dto.request.UIRequestBody;
import ru.cleverhause.util.JsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Service
public class BoardDataServiceImpl implements BoardDataService {
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

    private static final Logger logger = LoggerFactory.getLogger(BoardDataServiceImpl.class);

    @Override
    public boolean checkBoardNumber(BoardRequestBody boardRequestBody) {
        return true;
    }

    @Override
    public Board registerBoard(BoardRequestBody<DeviceStructure> boardRegReq) throws Exception {
        User user = userDao.findByUsername(boardRegReq.getUsername());
        Board board = new Board();
        board.setBoardUID(boardRegReq.getBoardUID());
        board.setBoardName("Kitchen");
        board.setUser(user);
        user.getBoards().add(board);

        String structureJson = JsonUtil.toJson(boardRegReq.getDevices());

        BoardStructure boardStructure = new BoardStructure();
        boardStructure.setStructure(structureJson);
        boardStructure.setBoard(board);
        try {
            boardStructureDao.save(boardStructure);
        } catch (Exception e) {
            logger.debug("Exception while save board structure! ", e);
        }

        BoardControlData boardControlData = new BoardControlData();
        boardControlData.setCreated(new Date());
        boardControlData.setBoard(board);

        List<DeviceControl> defaultControl = new ArrayList<>();
        List<DeviceStructure> devicesStructure = boardRegReq.getDevices();

        for (DeviceStructure singleDeviceStruct : devicesStructure) {
            DeviceControl control = new DeviceControl();
            control.setId(singleDeviceStruct.getId());
            control.setCtrlVal(new Double(singleDeviceStruct.getMin()));
            defaultControl.add(control);
        }
        boardControlData.setData(JsonUtil.toJson(defaultControl));
        try {
            boardControlDataDao.save(boardControlData);
        } catch (Exception e) {
            logger.debug("Exception while save board default ctrlVal data! ", e);
        }

        board.setStructure(boardStructure);
        board.setSavedData((List<BoardSavedData>) null);
        board.setControlData(boardControlData);

        return boardDao.save(board);
    }

    @Override
    public Long generateBoardUID() throws Exception {
        Random rand = new Random();
        int nextId;
        do {
            nextId = rand.nextInt(1000000) + 1000000;
        } while (boardDao.findByBoardUID((long) nextId) != null);

        return (long) nextId;
    }

    @Override
    public Board saveData(Long boardUID, BoardRequestBody<DeviceData> boardSaveReq) {
        logger.info("saveData operation");
        Board savedBoard = findByUID(boardUID);
        if (savedBoard != null) {
            List<DeviceData> persistDeviceList = boardSaveReq.getDevices();
            BoardSavedData persistData = new BoardSavedData();
            persistData.setCreated(new Date());
            persistData.setBoard(savedBoard);
            String persistJsonData = "";
            try {
                persistJsonData = JsonUtil.toJson(persistDeviceList);
            } catch (Exception e) {
                logger.debug("Exception while saveData when toJson converting! ", e);
            }
            persistData.setData(persistJsonData);
            savedBoard.getSavedData().add(persistData);
            boardSavedDataDao.save(persistData);
        }

        return savedBoard;
    }

    @Override
    public Board getData(Long boardUID) {
        return null;
    }

    @Override
    public Board saveControl(Long boardUID, UIRequestBody<InputBoardControls<DeviceControl>> boardControlReq) {
        logger.info("saveControl operation");
        Board savedBoard = findByUID(boardUID);
        if (savedBoard != null) {
            //TODO CHECK LIMITs of CONTROLs
            List<DeviceControl> persistDeviceControlsList = boardControlReq.getInput().getDevices();
            BoardControlData persistControl = boardControlDataDao.findByBoardId(savedBoard.getId());
            if (persistControl == null) {
                persistControl = new BoardControlData();
                persistControl.setBoard(savedBoard);
            }
            persistControl.setCreated(new Date());
            String persistControlDataJson = "";
            try {
                persistControlDataJson = JsonUtil.toJson(persistDeviceControlsList);
            } catch (Exception e) {
                logger.debug("Exception while saveData when toJson converting! ", e);
            }
            persistControl.setData(persistControlDataJson);
            savedBoard.setControlData(persistControl);
            boardControlDataDao.save(persistControl);
        }

        return savedBoard;
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
        logger.info("Get operation");
        return null;
    }
}
