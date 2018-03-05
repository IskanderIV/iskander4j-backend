package ru.cleverhause.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cleverhause.dao.RoleDao;
import ru.cleverhause.dao.UserDao;
import ru.cleverhause.model.Role;
import ru.cleverhause.model.User;

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

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private Md5PasswordEncoder md5PasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(md5PasswordEncoder.encodePassword(user.getPassword(), user.getUsername()));
        Set<Role> defRoles = new HashSet<Role>();
        defRoles.add(roleDao.getOne(1L));
        user.setRoles(defRoles);
        userDao.save(user);
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUsername(username);
    }
}
