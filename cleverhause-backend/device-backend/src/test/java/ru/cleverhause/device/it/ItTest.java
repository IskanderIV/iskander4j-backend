package ru.cleverhause.device.it;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.cleverhause.device.config.ApplicationContextConfig;

import javax.servlet.ServletContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "")
@ContextConfiguration(classes = {ApplicationContextConfig.class})
@ActiveProfiles("dev")
public class ItTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test() {
        ServletContext servletContext = wac.getServletContext();
        Assert.assertNotNull(servletContext);
    }
}
