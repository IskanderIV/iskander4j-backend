package ru.cleverhause.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cleverhause.users.dto.request.AddUserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, PostgresContainerExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsersControllerWithEmbeddedPostgresTest {

    private static final String CONTEXT_PATH = "/api/users";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getUserInfoHappyFlowTest() throws Exception {
        AddUserRequest newUser = AddUserRequest.builder()
                .username("Alise")
                .email("lala@mail.ru")
                .password("password")
                .build();
        String savedUser = mockMvc.perform(MockMvcRequestBuilders.post(CONTEXT_PATH + "/user")
                .content(MAPPER.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserInfoResponse userInfoResponse = MAPPER.readValue(savedUser, UserInfoResponse.class);

        mockMvc.perform(MockMvcRequestBuilders.get(CONTEXT_PATH + userInfoResponse.getUserId() + "/userInfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void addUserHappyFlowTest() {
        assertNotNull(entityManager);
        assertNotNull(context);
    }

    @Test
    void updateUserHappyFlowTest() {
    }

    @Test
    void deleteUserHappyFlowTest() {
    }
}