package ru.cleverhause.devices.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cleverhause.devices.TestUtils;
import ru.cleverhause.devices.dto.device.DeviceParamsDto;
import ru.cleverhause.devices.dto.request.DeviceParamsRequest;
import ru.cleverhause.devices.dto.response.DeviceControlResponse;
import ru.cleverhause.devices.dto.response.UserDevicesResponse;
import ru.cleverhause.devices.dto.sensor.SensorParamsDto;
import ru.cleverhause.devices.testcontainer.MongoContainerExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cleverhause.devices.TestUtils.fromFile;

@Slf4j
@ExtendWith({SpringExtension.class, MongoContainerExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DevicesControllerTest {
    private static final String TEST_DEVICE_NAME = "deviceName";
    private static final String TEST_USER_NAME = "username";
    private static final String TEST_DEVICE_ID = "deviceId";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDevicesParamsTest() throws Exception {
        SensorParamsDto sensorParamsDto = SensorParamsDto.sensorParamsDtoBuilder()
                .id(1)
                .min(0.0)
                .max(5.0)
                .adj(true)
                .discrete(0.5)
                .rotate(true)
                .signaling(false)
                .build();
        DeviceParamsRequest deviceParamsRequest = DeviceParamsRequest.deviceParamsRequestBuilder()
                .deviceName(TEST_DEVICE_NAME)
                .username(TEST_USER_NAME)
                .sensors(Collections.singletonList(sensorParamsDto))
                .build();
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/device/params")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(deviceParamsRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, String> responseMap = MAPPER.readValue(response, new TypeReference<>() {
        });
        String deviceId = responseMap.get("deviceId");

        assertTrue(StringUtils.isNotBlank(deviceId));

        log.info("Inserted deviceId: {}", deviceId);

        UserDevicesResponse expected = new UserDevicesResponse(Collections.singletonList(
                DeviceParamsDto.deviceParamsDtoBuilder()
                        .deviceId(deviceId)
                        .deviceName(TEST_DEVICE_NAME)
                        .username(TEST_USER_NAME)
                        .sensors(Collections.singletonList(sensorParamsDto))
                        .build()
        ));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/params?username={1}", TEST_USER_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(MAPPER.writeValueAsString(expected)));
    }

    @Test
    void getDevicesDataTest() throws Exception {
        //when
        String deviceId = saveTestDeviceData("json/request/DeviceDataRequest.json").getDeviceId();
        log.info("Inserted deviceId: {}", deviceId);

        //then, verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/data?ids={1}", deviceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(fromFile("json/response/DevicesDataResponse.json")));
    }

    private DeviceControlResponse saveTestDeviceData(String testDataPath) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/api/device/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromFile(testDataPath)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        DeviceControlResponse deviceControlResponse = MAPPER.readValue(response, new TypeReference<>() {});

        assertNotNull(deviceControlResponse);

        return deviceControlResponse;
    }

    @Test
    void updateDevicesControlTest() throws Exception {
        String deviceId = saveTestDeviceData("json/request/DeviceDataRequestForControlTest.json").getDeviceId();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/devices/control")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromFile("json/request/DevicesControlRequest.json")))
                .andExpect(status().isOk());

        //then, verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/data?ids={1}", deviceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtils.fromFile("json/response/DevicesDataResponseForControl.json")));
    }

    @Test
    void deleteDevicesTest() throws Exception {
        String deviceId = saveTestDeviceData("json/request/DeviceDataRequest.json").getDeviceId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/devices?ids={1}", deviceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        //then, verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/data?ids={1}", deviceId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtils.fromFile("json/response/DevicesDataResponseForDelete.json")));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/devices/params?username={1}", "ivanov")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestUtils.fromFile("json/response/DevicesParamsResponseForDelete.json")));
    }
}