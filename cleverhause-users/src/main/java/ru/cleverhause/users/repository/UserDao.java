package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cleverhause.users.entity.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    //TODO need first find do with id and if only it crashed nedd do second one with username
    @Query(value = "SELECT u FROM User u WHERE u.id = :id OR u.username = :username")
    Optional<User> findUser(@Param("id") Long id, @Param("username") String username);
}
