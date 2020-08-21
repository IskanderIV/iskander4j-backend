package ru.cleverhause.auth;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cleverhause.auth.config.SuccessOAuth2LoginHandler;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CleverhauseAuthApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SuccessOAuth2LoginHandler successOAuth2LoginHandler;

	@Value("${spring.security.oauth2.client.redirectUrl}")
	private String redirectUrl;

	@LocalServerPort
	int currentPort;

	@BeforeEach
	public void init() {
		String redirectUrlWithLocalPort = StringUtils.replaceOnce(redirectUrl, "{port}", Integer.toString(currentPort));
		successOAuth2LoginHandler.setRedirectUrl(redirectUrlWithLocalPort);
	}

	@Test
	void healthCheck() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(TestUtils.fromFile("responses/actuator_health_ok.json")));
	}

	@Test
	void formLogin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(TestUtils.fromFile("responses/actuator_health_ok.json")));
	}
}