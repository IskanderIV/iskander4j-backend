package ru.cleverhause.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cleverhause.auth.entity.User;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 3/29/2020.
 */

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
