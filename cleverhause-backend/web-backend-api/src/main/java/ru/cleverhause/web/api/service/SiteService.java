package ru.cleverhause.web.api.service;

import org.springframework.lang.Nullable;
import ru.cleverhause.web.api.dto.request.form.DeviceOnDevicesJspForm;
import ru.cleverhause.web.api.dto.request.form.NewBoardUidForm;
import ru.cleverhause.web.api.dto.request.page.BoardDto_MyBoardsJsp;

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

    List<DeviceOnDevicesJspForm> getDevicesDtoByBoardUID(@Nullable String boardUID) throws IOException;

    boolean updateBoardControl(@Nullable String boardUID, final List<DeviceOnDevicesJspForm> passedDeviceList) throws Exception;

    NewBoardUidForm generateBoardUID() throws Exception;
}
