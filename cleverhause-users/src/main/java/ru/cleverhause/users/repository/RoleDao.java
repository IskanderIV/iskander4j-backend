package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.entity.RoleEntity;

public interface RoleDao extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRolename(String roleName);
}
