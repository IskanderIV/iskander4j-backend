package ru.cleverhause.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.auth.entity.Role;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/29/2020.
 */

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRolename(String roleName);
}
