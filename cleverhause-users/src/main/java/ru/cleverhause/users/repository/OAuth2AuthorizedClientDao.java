package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientEntity;
import ru.cleverhause.users.entity.OAuth2AuthorizedClientId;

public interface OAuth2AuthorizedClientDao extends JpaRepository<OAuth2AuthorizedClientEntity, OAuth2AuthorizedClientId> {

}
