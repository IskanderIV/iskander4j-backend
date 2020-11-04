package ru.cleverhause.users.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;
import ru.cleverhause.users.repository.UserDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.util.Locale.Category.DISPLAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final UserDao userDao;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        List<UserDetails> users = loadUsersByUsername(username);

        if (users.size() == 0) {
            log.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException(
                    messageSource.getMessage("JdbcDaoImpl.notFound",
                            new Object[]{username}, Locale.getDefault(DISPLAY)));
        }

        UserDetails user = users.get(0); // contains no GrantedAuthority[]

        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();
        dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
        dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));

        if (dbAuthsSet.size() == 0) {
            log.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

            throw new UsernameNotFoundException(messageSource.getMessage(
                    "JdbcDaoImpl.noAuthority", new Object[]{username},
                    Locale.getDefault(DISPLAY)));
        }

        return createUserDetails(username, user, dbAuthsSet);
    }

    /**
     * Executes the SQL <tt>usersByUsernameQuery</tt> and returns a list of UserDetails
     * objects. There should normally only be one matching user.
     */
    private List<UserDetails> loadUsersByUsername(String username) {
        String usersByUsernameQuery = "";
        return this.jdbcOperations.query(usersByUsernameQuery,
                new String[]{username}, (rs, rowNum) -> {
                    String username1 = rs.getString(1);
                    String password = rs.getString(2);
                    boolean enabled = rs.getBoolean(3);
                    return new User(username1, password, enabled, true, true, true,
                            AuthorityUtils.NO_AUTHORITIES);
                });
    }

    /**
     * Loads authorities by executing the SQL from <tt>authoritiesByUsernameQuery</tt>.
     *
     * @return a list of GrantedAuthority objects for the user
     */
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        return getJdbcTemplate().query(this.authoritiesByUsernameQuery,
                new String[]{username}, (rs, rowNum) -> {
                    String roleName = JdbcDaoImpl.this.rolePrefix + rs.getString(2);

                    return new SimpleGrantedAuthority(roleName);
                });
    }

    /**
     * Loads authorities by executing the SQL from
     * <tt>groupAuthoritiesByUsernameQuery</tt>.
     *
     * @return a list of GrantedAuthority objects for the user
     */
    protected List<GrantedAuthority> loadGroupAuthorities(String username) {
        return getJdbcTemplate().query(this.groupAuthoritiesByUsernameQuery,
                new String[]{username}, (rs, rowNum) -> {
                    String roleName = getRolePrefix() + rs.getString(3);

                    return new SimpleGrantedAuthority(roleName);
                });
    }

    /**
     * Can be overridden to customize the creation of the final UserDetailsObject which is
     * returned by the <tt>loadUserByUsername</tt> method.
     *
     * @param username            the name originally passed to loadUserByUsername
     * @param userFromUserQuery   the object returned from the execution of the
     * @param combinedAuthorities the combined array of authorities from all the authority
     *                            loading queries.
     * @return the final UserDetails which should be used in the system.
     */
    protected UserDetails createUserDetails(String username,
                                            UserDetails userFromUserQuery, Collection<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();

        if (!this.usernameBasedPrimaryKey) {
            returnUsername = username;
        }

        return new User(returnUsername, userFromUserQuery.getPassword(),
                userFromUserQuery.isEnabled(), userFromUserQuery.isAccountNonExpired(),
                userFromUserQuery.isCredentialsNonExpired(), userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
    }
}
