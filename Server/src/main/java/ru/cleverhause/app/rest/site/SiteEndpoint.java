package ru.cleverhause.app.rest.site;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.cleverhause.app.dto.page.UserBoard;
import ru.cleverhause.service.security.SecurityService;
import ru.cleverhause.service.site.SiteService;

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

    private static final Logger logger = Logger.getLogger(SiteEndpoint.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home(Map<String, Object> model) {
        String currUserName = securityService.findLoggedInUsername();
        model.put("principal", currUserName);
        return new ModelAndView("home", model);
    }

    @GetMapping(value = {"/myboard/myboard"})
    public ModelAndView myboard(Map<String, Object> model, @RequestParam(required = false) String username) {
        String currUserName = username;
        if (currUserName == null) {
            currUserName = securityService.findLoggedInUsername();
        }
        List<UserBoard> userBoards = siteService.getBoardsByUserName(currUserName);
        model.put("userBoards", userBoards);
        model.put("principal", currUserName);
        return new ModelAndView("myboard/myboard", model);
    }

    @GetMapping(value = {"/myboard/newboard"})
    public String newboard() {
        return "myboard/myboard";
    }

    @GetMapping(value = {"/myboard/board"})
    public ModelAndView board(Map<String, Object> model, @RequestParam(required = false) String boardname) {
        String currUserName = securityService.findLoggedInUsername();
        model.put("principal", currUserName);
        return new ModelAndView("myboard/board", model);
    }
}
