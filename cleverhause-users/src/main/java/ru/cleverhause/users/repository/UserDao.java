package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.users.entity.User;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/29/2020.
 */

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
