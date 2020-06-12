package ru.cleverhause.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Filter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cleverhause.users.dto.request.UserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, PostgresContainerExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
// TODO it does not work because spring-security-test is still in the classpath and he does it's autoconfiguration for csrf protection
class UsersControllerWithEmbeddedPostgresTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getUserInfoHappyFlowTest() throws Exception {
        UserRequest newUser = UserRequest.builder()
                .email("lala@mail.ru")
                .password("password")
                .build();
        String savedUser = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/user")
                .content(MAPPER.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserInfoResponse userInfoResponse = MAPPER.readValue(savedUser, UserInfoResponse.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + userInfoResponse.getUserId() + "/userInfo")
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