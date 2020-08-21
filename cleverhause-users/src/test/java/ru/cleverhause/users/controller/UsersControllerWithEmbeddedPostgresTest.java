package ru.cleverhause.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import ru.cleverhause.users.dto.request.AddUserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;

import javax.persistence.EntityManager;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {UsersControllerWithEmbeddedPostgresTest.PropertiesInitializer.class})
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

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            postgresContainer.copyFileToContainer(
                    MountableFile.forClasspathResource("/script/init-users-db.sh"),
                    "/docker-entrypoint-initdb.d/init-users-db.sh");
            postgresContainer.copyFileToContainer(
                    MountableFile.forClasspathResource("/script/initUsersDb.sql"),
                    "/var/lib/postgresql/init/initUsersDb.sql");
            org.testcontainers.containers.Container.ExecResult lsResult = postgresContainer.execInContainer("chmod", "u+x", "/docker-entrypoint-initdb.d/init-users-db.sh");
            lsResult = postgresContainer.execInContainer("bash", "/docker-entrypoint-initdb.d/init-users-db.sh");
            log.info("Result2: {}", lsResult);
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword())
                    .applyTo(applicationContext.getEnvironment());
        }
    }

//    @DynamicPropertySource
//    static void databaseProperties(DynamicPropertyRegistry registry) {
////        registry.add("spring.datasource.database", postgresContainer::getDatabaseName);
////        registry.add("spring.datasource.username", postgresContainer::getUsername);
////        registry.add("spring.datasource.password", postgresContainer::getPassword);
//    }

    @BeforeClass
    public static void globalInit() throws IOException, InterruptedException {
//        postgresContainer.start();
////        postgresContainer.withEnv(Map.of("POSTGRES_DB", postgresContainer.getD));
////        postgresContainer.withEnv("POSTGRES_URL", postgresContainer.getJdbcUrl());
////        postgresContainer.withInitScript("script/initUsersDb.sql");
//        postgresContainer.copyFileToContainer(
//                MountableFile.forClasspathResource("/script/init-users-db.sh"),
//                "/docker-entrypoint-initdb.d/init-users-db.sh");
//        postgresContainer.copyFileToContainer(
//                MountableFile.forClasspathResource("/script/initUsersDb.sql"),
//                "/var/lib/postgresql/init/initUsersDb.sql");
//        org.testcontainers.containers.Container.ExecResult lsResult = postgresContainer.execInContainer("chmod", "u+x", "/docker-entrypoint-initdb.d/init-users-db.sh");
//        log.info("Result2: {}", lsResult);
    }

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
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserInfoResponse userInfoResponse = MAPPER.readValue(savedUser, UserInfoResponse.class);

        String userInfo = mockMvc.perform(MockMvcRequestBuilders.get(CONTEXT_PATH + "/" + userInfoResponse.getUsername() + "/info")
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