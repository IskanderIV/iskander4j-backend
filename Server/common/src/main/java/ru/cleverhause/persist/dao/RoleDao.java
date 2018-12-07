package ru.cleverhause.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.persist.entities.Role;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/2/2018.
 */

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByRolename(String roleName);
}
