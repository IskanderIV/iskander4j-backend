package ru.cleverhause.service.arduino;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.dao.BoardControlDataDao;
import ru.cleverhause.dao.BoardDao;
import ru.cleverhause.dao.BoardSavedDataDao;
import ru.cleverhause.dao.UserDao;
import ru.cleverhause.rest.board.dto.request.BoardReq;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Service
public class ArduinoDataServiceImpl implements ArduinoDataService {
    @Autowired
    private BoardDao boardDao;
    @Autowired
    private BoardControlDataDao boardControlDataDao;
    @Autowired
    private BoardSavedDataDao boardSavedDataDao;
    @Autowired
    private UserDao userDao;

    private static final Logger logger = Logger.getLogger(ArduinoDataServiceImpl.class);

    @Override
    public boolean checkBoardNumber(BoardReq boardReq) {
        return true;
    }

    @Override
    public boolean save(BoardReq boardReq) {

        logger.info("PUT operation");
        return true;
    }

    @Override
    public List<BoardReq> getLast(int num) {
        boardDao.findByBoardName()
        int capacity = arduinoDataRepository.size();
        if (capacity > 0) {
            for (String key : arduinoDataRepository.keySet()) {
                return arduinoDataRepository.get(key);
            }
        }
        logger.info("Get operation");
        return null;
    }
}
