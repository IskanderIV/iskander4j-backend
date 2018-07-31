package ru.cleverhause.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.cleverhause.persist.dao.UserDao;
import ru.cleverhause.persist.entities.Role;
import ru.cleverhause.persist.entities.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Service for {@link org.springframework.security.core.userdetails.UserDetailsService} interface
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version 1.0.0
 * @date 3/4/2018.
 */

@Component
@Profile("prod")
public class UserDetailsServiceImpl implements UserDetailsService {

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
