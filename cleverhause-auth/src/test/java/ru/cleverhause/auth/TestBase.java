package ru.cleverhause.auth;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static ru.cleverhause.auth.RestStubFactory.getStub;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public abstract class TestBase {

    protected final RestStub gitHubGetToken = getStub("responses/json/gitHub_get_token_OK.json",
            "/login/oauth2/code/github");
    protected final RestStub gitHubGetUserInfo = getStub("responses/json/gitHub_get_userinfo_OK.json",
            "/user");
}
