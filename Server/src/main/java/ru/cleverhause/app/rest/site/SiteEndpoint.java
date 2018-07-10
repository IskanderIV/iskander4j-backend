package ru.cleverhause.app.rest.site;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cleverhause.service.security.SecurityService;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 6/30/2018.
 */
@RestController
@RequestMapping(value = "/site",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class SiteEndpoint {

    private static final Logger logger = Logger.getLogger(SiteEndpoint.class);

    @Autowired
    private SecurityService securityService;

    @GetMapping(value = {"/home"})
    public String home() {
        return "HomeGet";
    }

    // TODO Test only
    @PostMapping(value = {"/home"})
    public String homePost() {
        return "HomePost";
    }
}
