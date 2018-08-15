package ru.cleverhause.service.site;

import org.springframework.lang.Nullable;
import ru.cleverhause.app.dto.page.BoardDto_MyBoardsJsp;
import ru.cleverhause.app.dto.page.DeviceDto_DevicesJsp;

import java.io.IOException;
import java.util.List;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 8/9/2018.
 */
public interface SiteService {
    List<BoardDto_MyBoardsJsp> getBoardsByUserName(@Nullable String username);

    List<DeviceDto_DevicesJsp> getDevicesByBoardUID(@Nullable String boardUID) throws IOException;
}
