package ru.cleverhause.common.persist.api.entity.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cleverhause.common.persist.api.entity.Board;
import ru.cleverhause.common.persist.api.entity.User;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/12/2018.
 */
public class EntityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EntityUtils.class);

    public static String getBoardJsonStructure(Board board) {
        if (board != null && board.getStructure() != null) {
            return board.getStructure().getStructure();
        }
        return StringUtils.EMPTY;
    }

    public static void addBoardToUser(User user, Board board) {
        if (user != null && user.getBoards() != null && board != null) {
            user.getBoards().add(board);
        }
    }
}
