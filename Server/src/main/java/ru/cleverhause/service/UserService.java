package ru.cleverhause.service;

import ru.cleverhause.model.User;

/**
 * Service for {@link User}
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version 1.0.0
 * @date 12/2/2017.
 */
public interface UserService {

    void save(User user);

    User findByUserName(String username);
}
