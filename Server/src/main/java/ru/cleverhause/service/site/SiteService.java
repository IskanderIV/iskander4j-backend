package ru.cleverhause.service.site;

import ru.cleverhause.app.dto.page.UserBoard;

import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */
public interface SiteService {
    List<UserBoard> getBoardsByUserName(String username);
}
