package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.authorities.Role;
import ru.cleverhause.users.entity.RoleEntity;

import java.util.Optional;

public interface RoleDao extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByRolename(String roleName);
}
