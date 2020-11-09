package ru.cleverhause.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cleverhause.users.entity.UserEntity;

import java.util.Optional;

public interface UserDao extends JpaRepository<UserEntity, Long> {

//    @Query(value = "SELECT u.id, u.username, u.email FROM UserEntity u WHERE u.username = :username", nativeQuery = true)
    Optional<UserEntity> findByUsername(String username);

//    @Query(value = "SELECT u FROM UserEntity u WHERE u.id = :id OR u.username = :username")
//    Optional<UserEntity> findUser(@Param("id") Long id, @Param("username") String username);
}
