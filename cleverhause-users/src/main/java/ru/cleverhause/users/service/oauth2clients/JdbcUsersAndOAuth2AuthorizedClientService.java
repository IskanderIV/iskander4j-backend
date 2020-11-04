package ru.cleverhause.users.service.oauth2clients;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientEntity;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientId;
import ru.cleverhause.users.entity.UserEntity;
import ru.cleverhause.users.entity.mapper.AuthorizedClientMapper;
import ru.cleverhause.users.repository.OAuth2AuthorizedClientDao;
import ru.cleverhause.users.repository.UserDao;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JdbcUsersAndOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final UserDao userDao;
    private final OAuth2AuthorizedClientDao authorizedClientDao;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AuthorizedClientMapper authorizedClientMapper;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        OAuth2AuthorizedClientEntity loaded = authorizedClientDao.findById(new OAuth2AuthorizedClientId(clientRegistrationId, principalName)).orElse(null);
        return authorizedClientMapper.from(loaded, clientRegistrationRepository);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        OAuth2AuthorizedClientEntity forSaving = authorizedClientMapper.from(authorizedClient, principal);
        String principalName = principal.getName();
        Optional<UserEntity> optionalSaved = userDao.findByUsername(principalName);
        optionalSaved.ifPresentOrElse(user -> {
            updateUser();
        }, () -> {
            UserEntity fictiveUser = UserEntity.builder()
                    .
                    .build();
        });
        forSaving.setUser();
        authorizedClientDao.save(forSaving);
    }

    private void updateUser() {
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        authorizedClientDao.deleteById(new OAuth2AuthorizedClientId(clientRegistrationId, principalName));
    }

}
