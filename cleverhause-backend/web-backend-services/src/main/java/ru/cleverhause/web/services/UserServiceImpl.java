package ru.cleverhause.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cleverhause.common.persist.api.entity.Role;
import ru.cleverhause.common.persist.api.entity.User;
import ru.cleverhause.common.persist.api.repository.RoleDao;
import ru.cleverhause.common.persist.api.repository.UserDao;
import ru.cleverhause.web.api.service.UserService;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link UserService} interface.
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/2/2017.
 */
@Service
public class UserServiceImpl implements UserService {
    public static final String DEF_ROLE = "ROLE_USER";

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> defRoles = new HashSet<Role>();
        defRoles.add(roleDao.findByRolename(DEF_ROLE));
        user.setRoles(defRoles);
        userDao.save(user);

    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUsername(username);
    }
}
