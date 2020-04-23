package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.entity.Role;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/29/2020.
 */

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRolename(String roleName);
}