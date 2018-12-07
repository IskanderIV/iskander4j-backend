package ru.cleverhause.persist.utils;

import org.apache.commons.lang3.StringUtils;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.persist.entities.User;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/12/2018.
 */
public class EntityUtils {

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
