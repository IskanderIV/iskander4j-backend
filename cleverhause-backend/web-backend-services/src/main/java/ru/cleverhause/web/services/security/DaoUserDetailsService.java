package ru.cleverhause.web.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.cleverhause.common.persist.api.entity.Role;
import ru.cleverhause.common.persist.api.entity.User;
import ru.cleverhause.common.persist.api.repository.UserDao;

import java.util.HashSet;
import java.util.Set;

/**
 * Service for {@link UserDetailsService} interface
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version 1.0.0
 * @date 3/4/2018.
 */

public class DaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
