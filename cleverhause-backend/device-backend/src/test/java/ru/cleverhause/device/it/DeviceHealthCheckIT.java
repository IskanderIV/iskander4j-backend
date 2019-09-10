package ru.cleverhause.device.it;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.cleverhause.device.config.DeviceAppConfig;
import ru.cleverhause.device.config.mvc.DeviceWebMvcConfig;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "")
@ContextConfiguration(classes = {DeviceAppConfig.class, DeviceWebMvcConfig.class})
@ActiveProfiles("dev")
public class DeviceHealthCheckIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceHealthCheckIT.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test() throws Exception {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
        LOGGER.debug("Servlet context = {}", servletContext);

        this.mockMvc.perform(get("/board/healthcheck")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
