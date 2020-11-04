package ru.cleverhause.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2AuthorizedClientId {
    private String clientRegistrationId;
    private String principalName;
}
