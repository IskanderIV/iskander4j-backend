package ru.cleverhause.app.rest.site;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.cleverhause.service.security.SecurityService;

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
    private SecurityService securityService;

    @GetMapping(value = {"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping(value = {"/myboard/myboard"})
    public String myboard() {
        return "myboard/myboard";
    }


    @GetMapping(value = {"/myboard/newboard"})
    public String newboard() {
        return "myboard/myboard";
    }
}
