package ru.cleverhause.service.site;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cleverhause.app.dto.page.UserBoard;
import ru.cleverhause.persist.entities.Board;
import ru.cleverhause.persist.entities.User;
import ru.cleverhause.service.board.BoardDataService;
import ru.cleverhause.service.user.UserService;

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
    public List<UserBoard> getBoardsByUserName(String username) {
        User user = userService.findByUserName(username);
        List<UserBoard> userBoards = Lists.newArrayList();
        for (Board board : user.getBoards()) {
            UserBoard userBoard = new UserBoard();
            userBoard.setName(board.getBoardName());
            userBoards.add(userBoard);
        }

        return userBoards;
    }
}
