package ru.cleverhause.api.service.site;

import org.springframework.lang.Nullable;
import ru.cleverhause.api.dto.form.DeviceOnDevicesJspForm;
import ru.cleverhause.api.dto.page.BoardDto_MyBoardsJsp;

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
}
