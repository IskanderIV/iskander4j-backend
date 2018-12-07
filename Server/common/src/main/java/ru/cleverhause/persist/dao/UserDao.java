package ru.cleverhause.persist.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.persist.entities.User;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/2/2018.
 */

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
