package ru.cleverhause.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AuthorizedClientId implements Serializable {
    private String clientRegistrationId;
    private String principalName;
}
