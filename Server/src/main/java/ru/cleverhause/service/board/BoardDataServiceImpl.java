package ru.cleverhause.service.board;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.dao.BoardControlDataDao;
import ru.cleverhause.dao.BoardDao;
import ru.cleverhause.dao.BoardSavedDataDao;
import ru.cleverhause.dao.UserDao;
import ru.cleverhause.model.Board;
import ru.cleverhause.model.BoardSavedData;
import ru.cleverhause.model.BoardStructure;
import ru.cleverhause.rest.board.dto.request.BoardReq;
import ru.cleverhause.rest.board.dto.request.registration.DeviceSetting;
import ru.cleverhause.rest.board.dto.request.work.DeviceData;
import ru.cleverhause.rest.board.dto.structure.DeviceStructure;
import ru.cleverhause.util.JsonUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private UserDao userDao;

    private static final Logger logger = Logger.getLogger(BoardDataServiceImpl.class);

    @Override
    public boolean checkBoardNumber(BoardReq boardReq) {
        return true;
    }

    @Override
    public Board saveBoardStructure(String boardUID, List<DeviceSetting> boardReq) throws Exception {
        Board board = findByUID(boardUID);
        if (board != null) {
            BoardStructure boardStructure = new BoardStructure(board.getId(), JsonUtil.toJson(boardReq), board);
            board.setStructure(boardStructure);
            board = boardDao.save(board);
        }

        return board;
    }

    @Override
    public Board saveData(String boardUID, BoardReq<DeviceData> boardSaveReq) {
        logger.info("saveData operation");
        Board savedBoard = findByUID(boardUID);
        if (savedBoard != null) {
            BoardStructure boardStructure = savedBoard.getStructure();
            List<DeviceStructure> deviceStructureList = getDeviceStructList(boardStructure);
            if (deviceStructureList != null) {
                dsLoop:
                for (DeviceStructure ds : deviceStructureList) {
                    List<BoardSavedData> savedDataList = savedBoard.getSavedData();
                    for (BoardSavedData sd : savedDataList) {
                        if (ds.getId() == sd.getId()) {
                            sd.setData();
                            continue dsLoop;
                        }
                    }

                }
            }
        }

        board.setSavedData(Arrays.asList(
                new BoardSavedData(board.getId(), JsonUtil.toJson(boardSaveReq.getDevices()), new Date(), board)
        ));

        return boardDao.save(board);
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
    public Board getData(String boardUID){
        return null;
    }

    @Override
    public Board saveControl(String boardUID, BoardReq<DeviceData> boardSaveReq) {
        return null;
    }

    @Override
    public Board getControl(Board board) {
        return null;
    }

    @Override
    public Board findByUID(String boardUID) {
        return boardDao.findByBoardUID(boardUID);
    }

    @Override
    public List<BoardReq> getLast(int num) {
        logger.info("Get operation");
        return null;
    }
}
