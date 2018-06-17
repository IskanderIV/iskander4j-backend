package ru.cleverhause.service.board;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.dao.BoardControlDataDao;
import ru.cleverhause.dao.BoardDao;
import ru.cleverhause.dao.BoardSavedDataDao;
import ru.cleverhause.dao.BoardStructureDao;
import ru.cleverhause.dao.UserDao;
import ru.cleverhause.model.Board;
import ru.cleverhause.model.BoardSavedData;
import ru.cleverhause.model.BoardStructure;
import ru.cleverhause.model.User;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.rest.board.dto.structure.DeviceStructure;
import ru.cleverhause.util.JsonUtil;

import java.util.Arrays;
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

    private static final Logger logger = Logger.getLogger(BoardDataServiceImpl.class);

    @Override
    public boolean checkBoardNumber(BoardReq boardReq) {
        return true;
    }

    @Override
    public Board registerBoard(BoardReq<DeviceStructure> boardRegReq) throws Exception {
        User user = userDao.findByUsername(boardRegReq.getUsername());
        Board board = new Board();
        board.setBoardUID(boardRegReq.getBoardUID());
        board.setBoardName("Kitchen");
        board.setUser(user);
        user.getBoards().add(board);
        return boardDao.save(board);
//        String structureJson = JsonUtil.toJson(boardRegReq.getDevices());
//
//        BoardStructure boardStructure = new BoardStructure();
//        boardStructure.setStructure(structureJson);
//
//        BoardControlData boardControlData = new BoardControlData();
//        boardControlData.setCreated(new Date());
//
//        List<DeviceControl> devicesControl = new ArrayList<>();
//        List<DeviceStructure> devicesStructure = boardRegReq.getDevices();
//
//        for (DeviceStructure dev : devicesStructure) {
//            DeviceControl control = new DeviceControl();
//            control.setControl(new Double(dev.getMin()));
//            devicesControl.add(control);
//        }
//        boardControlData.setData(JsonUtil.toJson(devicesControl));
//
//        Board board = new Board(boardRegReq.getBoardUID(), "", boardStructure, Arrays.asList(new BoardSavedData()), boardControlData, user);
//        return boardDao.save(board);
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
    public Board saveData(Long boardUID, BoardReq<DeviceData> boardSaveReq) throws Exception {
        logger.info("saveData operation");
        Board savedBoard = findByUID(boardUID);
        if (savedBoard != null) {
            BoardStructure boardStructure = savedBoard.getStructure();

            List<BoardSavedData> boardSavedData = savedBoard.getSavedData();
            List<DeviceData> actualDataList = boardSaveReq.getDevices();
            List<DeviceStructure> deviceStructureList = getDeviceStructList(boardStructure);
            savedBoard.setSavedData(Arrays.asList(
                    new BoardSavedData(JsonUtil.toJson(boardSaveReq.getDevices()), new Date(), savedBoard)));
            savedBoard = boardDao.save(savedBoard);
        }

        return savedBoard;
    }

    private List<DeviceStructure> getDeviceStructList(BoardStructure boardStructure) {
        List<DeviceStructure> deviceStructure = null;
        try {
            deviceStructure = Arrays.asList(JsonUtil.fromString(boardStructure.getStructure(), DeviceStructure[].class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceStructure;
    }

    @Override
    public Board getData(Long boardUID) {
        return null;
    }

    @Override
    public Board saveControl(Long boardUID, BoardReq<DeviceData> boardSaveReq) {
        return null;
    }

    @Override
    public Board getControl(Board board) {
        return null;
    }

    @Override
    public Board findByUID(Long boardUID) {
        return boardDao.findByBoardUID(boardUID);
    }

    @Override
    public List<BoardReq> getLast(int num) {
        logger.info("Get operation");
        return null;
    }
}
