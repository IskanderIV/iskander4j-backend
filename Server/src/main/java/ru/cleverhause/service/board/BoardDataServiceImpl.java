package ru.cleverhause.service.board;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.app.dto.DeviceControl;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.DeviceStructure;
import ru.cleverhause.app.dto.form.NewBoardUidForm;
import ru.cleverhause.app.dto.request.BoardRequestBody;
import ru.cleverhause.app.dto.request.InputBoardControls;
import ru.cleverhause.app.dto.request.UIRequestBody;
import ru.cleverhause.persist.dao.BoardControlDataDao;
import ru.cleverhause.persist.dao.BoardDao;
import ru.cleverhause.persist.dao.BoardSavedDataDao;
import ru.cleverhause.persist.dao.BoardStructureDao;
import ru.cleverhause.persist.dao.NewBoardUidDao;
import ru.cleverhause.persist.dao.UserDao;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.persist.entities.BoardControlData;
import ru.cleverhause.persist.entities.BoardSavedData;
import ru.cleverhause.persist.entities.BoardStructure;
import ru.cleverhause.persist.entities.NewBoardUID;
import ru.cleverhause.persist.entities.User;
import ru.cleverhause.persist.utils.EntityUtils;
import ru.cleverhause.service.board.converter.NewBoardUID_To_NewBoardUidFormConverter;
import ru.cleverhause.service.security.SecurityService;
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

    @Autowired
    private NewBoardUidDao newBoardUidDao;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private NewBoardUID_To_NewBoardUidFormConverter newBoardUidToUidFormConverter;

    private static final Logger logger = LoggerFactory.getLogger(BoardDataServiceImpl.class);

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

            try {
                boardDao.save(newBoard);

                // delete newBoardUidEntity if "save" operation was succeed
                if (boardDao.findByBoardUID(passedBoardUID) != null) {
                    user.setNewBoardUID(null);
                    savedBoardUidEntity.setUser(null);
                    newBoardUidDao.delete(savedBoardUidEntity);
                }

                return true;

            } catch (Exception e) {
                logger.debug("Exception while saving board structure! ", e);

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
                    logger.debug("Exception while updating board structure! ", e);
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
                return null;
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
