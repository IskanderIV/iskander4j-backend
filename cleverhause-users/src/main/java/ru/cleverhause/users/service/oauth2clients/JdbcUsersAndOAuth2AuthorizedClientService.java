package ru.cleverhause.users.service.oauth2clients;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.cleverhause.users.authorities.Role;
import ru.cleverhause.users.config.properties.AuthorizedClientsAttributes;
import ru.cleverhause.users.config.properties.ClientRegistrationMainAttributeTags;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientEntity;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientId;
import ru.cleverhause.users.entity.Profile;
import ru.cleverhause.users.entity.RoleEntity;
import ru.cleverhause.users.entity.UserEntity;
import ru.cleverhause.users.entity.mapper.AuthorizedClientMapper;
import ru.cleverhause.users.repository.OAuth2AuthorizedClientDao;
import ru.cleverhause.users.repository.RoleDao;
import ru.cleverhause.users.repository.UserDao;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ru.cleverhause.users.config.properties.AuthorizedClientsAttributes.email;

@Service
@RequiredArgsConstructor
public class JdbcUsersAndOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final UserDao userDao;
    private final OAuth2AuthorizedClientDao authorizedClientDao;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final ClientRegistrationMainAttributeTags mainAttributesTags;
    private final AuthorizedClientMapper authorizedClientMapper;
    private final RoleDao roleDao;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        OAuth2AuthorizedClientEntity loaded = authorizedClientDao.findById(new OAuth2AuthorizedClientId(clientRegistrationId, principalName)).orElse(null);
        return authorizedClientMapper.from(loaded, clientRegistrationRepository);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
        OAuth2AuthorizedClientEntity forSaving = authorizedClientMapper.from(authorizedClient, principal);
        String principalName = principal.getName();
        var clientAttributes = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttributes();
        var clientAttributesTagsMap = mainAttributesTags.getByClientId(clientRegistrationId);
        ClientMainAttributesHolder clientMainAttributesHolder = new ClientMainAttributesHolderImpl(clientAttributes, clientAttributesTagsMap);

        var savedAuthorizedClient =
                authorizedClientDao.findById(new OAuth2AuthorizedClientId(clientRegistrationId, principalName));

        savedAuthorizedClient.ifPresentOrElse(saClient -> {
            UserEntity savedUser = saClient.getUser();
            updateUser(savedUser, clientMainAttributesHolder);
            updateAuthoizedClient(saClient, forSaving);
            authorizedClientDao.save(saClient);
        }, () -> {
            String username = clientRegistrationId + "_" + principalName;
            Optional<UserEntity> optionalSaved = userDao.findByUsername(username);
            optionalSaved.ifPresentOrElse(user -> updateUser(user, clientMainAttributesHolder), () -> {
                Profile profile = createNewProfile();
                RoleEntity role = roleDao.findByRolename(Role.ROLE_USER.name()).orElse(new RoleEntity());
                UserEntity fictiveUser = UserEntity.builder()
                        .username(username)
                        .password(RandomStringUtils.randomAlphanumeric(20))
                        .email(StringUtils.EMPTY)
                        .profile(profile)
                        .roles(Set.of(role))
                        .build();
                profile.setUser(fictiveUser);
                forSaving.setUser(fictiveUser);
                role.setUsers(Set.of(fictiveUser));
                userDao.save(fictiveUser);
                authorizedClientDao.save(forSaving);
                userDao.flush();
                authorizedClientDao.flush();
            });
        });
    }

    private void updateAuthoizedClient(OAuth2AuthorizedClientEntity saClient, OAuth2AuthorizedClientEntity forSaving) {
        saClient.setAccessTokenExpiresAt(forSaving.getAccessTokenExpiresAt());
        saClient.setAccessTokenIssuedAt(forSaving.getAccessTokenIssuedAt());
        saClient.setAccessTokenValue(forSaving.getAccessTokenValue());
        saClient.setAccessTokenScopes(forSaving.getAccessTokenScopes());
        saClient.setAccessTokenType(forSaving.getAccessTokenType());
    }

    private void updateUser(@Nonnull final UserEntity user, ClientMainAttributesHolder clientMainAttributesHolder) {
        user.setEmail(clientMainAttributesHolder.getMainAttribute(email));
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = createNewProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        profile.setIsExpired(false);
        profile.setIsLoggedIn(true);
        userDao.saveAndFlush(user);
    }

    private Profile createNewProfile() {
        return Profile.builder()
                .address(StringUtils.EMPTY)
                .isLoggedIn(true)
                .lastLoginTime(Instant.now())
                .phone(StringUtils.EMPTY)
                .isExpired(false)
                .build();
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        authorizedClientDao.deleteById(new OAuth2AuthorizedClientId(clientRegistrationId, principalName));
    }

    private static class ClientMainAttributesHolderImpl implements ClientMainAttributesHolder {
        Map<AuthorizedClientsAttributes, String> clientMainAttributesMap = new HashMap<>();

        /**
         * Выбираем из всех аттрибутов клиента все прописанные для него в пропертях
         *
         * @param clientAttributes        - все аттрибуты клиента
         * @param clientAttributesTagsMap - наименование основных аттрибутов для данного клиента
         */
        public ClientMainAttributesHolderImpl(Map<String, Object> clientAttributes,
                                              Map<AuthorizedClientsAttributes, String> clientAttributesTagsMap) {
            if (!CollectionUtils.isEmpty(clientAttributesTagsMap)) {
                Arrays.stream(AuthorizedClientsAttributes.values()).forEach(
                        attTag -> {
                            Object attribute = clientAttributes.get(clientAttributesTagsMap.get(attTag));
                            clientMainAttributesMap.put(attTag, attribute != null ? attribute.toString() : null);
                        });
            }
        }

        @Override
        public String getMainAttribute(AuthorizedClientsAttributes mainAttr) {
            if (mainAttr == null) {
                return null;
            }
            return clientMainAttributesMap.get(mainAttr);
        }
    }

    private interface ClientMainAttributesHolder {
        String getMainAttribute(AuthorizedClientsAttributes mainAttr);
    }

}
