package ru.cleverhause.web.api.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cleverhause.common.api.service.security.SecurityService;
import ru.cleverhause.web.api.dto.request.form.DeviceList_DevicesJspForm;
import ru.cleverhause.web.api.dto.request.form.NewBoardUidForm;
import ru.cleverhause.web.api.dto.request.page.BoardDto_MyBoardsJsp;
import ru.cleverhause.web.api.service.SiteService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/30/2018.
 */
@Controller
public class SiteEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteEndpoint.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home(Map<String, Object> model) {
        LOGGER.debug("Calling /home endpoint");
        String currUserName = securityService.findLoggedInUsername();
        LOGGER.info("Current user {}", currUserName);
        model.put("principal", currUserName);
        return new ModelAndView("home", model);
    }

    @GetMapping(value = {"/contacts"})
    public ModelAndView contacts(Map<String, Object> model) {
        String currUserName = securityService.findLoggedInUsername();
        model.put("principal", currUserName);
        return new ModelAndView("contacts", model);
    }

    @GetMapping(value = {"/myboard/myboard"})
    public ModelAndView myBoards(Map<String, Object> model, @RequestParam(required = false) String username) {
        String currUserName = securityService.findLoggedInUsername();
        List<BoardDto_MyBoardsJsp> userBoards = siteService.getBoardsByUserName(currUserName);

        model.put("userBoards", userBoards);
        model.put("principal", currUserName);

        return new ModelAndView("myboard/myboard", model);
    }

    @GetMapping(value = {"/myboard/newboard"})
    public ModelAndView newBoardGet(Map<String, Object> model) {
        String currUserName = securityService.findLoggedInUsername();
        model.put("principal", currUserName);
        model.put("newBoardUidForm", new NewBoardUidForm());
        return new ModelAndView("myboard/newboard", model);
    }

    @PostMapping(value = {"/myboard/newboard"})
    public ModelAndView newBoardPost(Map<String, Object> model) throws Exception {
        NewBoardUidForm newBoardUidForm = siteService.generateBoardUID();
        String currUserName = securityService.findLoggedInUsername();
        model.put("principal", currUserName);
        model.put("newBoardUidForm", newBoardUidForm);
        return new ModelAndView("myboard/newboard", model);
    }

    @GetMapping(value = {"/myboard/board"})
    public ModelAndView board(Map<String, Object> model, @RequestParam String boardUID) throws IOException {
        DeviceList_DevicesJspForm devices = new DeviceList_DevicesJspForm();
        devices.setDevices(siteService.getDevicesDtoByBoardUID(boardUID));
        String currUserName = securityService.findLoggedInUsername();
        model.put("deviceListForm", devices);
        model.put("deviceNumber", devices.getDevices().size());
        model.put("principal", currUserName);

        return new ModelAndView("myboard/board", model);
    }

    @PostMapping(value = {"/myboard/board"})
    public String boardControl(
            @ModelAttribute(name = "deviceListForm") DeviceList_DevicesJspForm deviceListForm,
            RedirectAttributes redirectAttributes,
            @RequestParam String boardUID,
            @RequestParam(name = "boardname") String boardName) throws Exception {

        boolean updateResult = siteService.updateBoardControl(boardUID, deviceListForm.getDevices());

        return "redirect:/myboard/board?boardUID=" + boardUID + "&boardname=" + boardName;
    }
}
