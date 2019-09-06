package ru.cleverhause.device.config.mvc;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.cleverhause.device.config.ApplicationContextConfig;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContextConfig.class})
public class DeviceWebMvcConfigTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}