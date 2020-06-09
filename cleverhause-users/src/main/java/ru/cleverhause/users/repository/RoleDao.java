package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.entity.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRolename(String roleName);
}
