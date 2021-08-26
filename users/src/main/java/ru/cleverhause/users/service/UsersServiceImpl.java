package ru.cleverhause.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cleverhause.users.authorities.Role;
import ru.cleverhause.users.dto.request.AddUserRequest;
import ru.cleverhause.users.dto.request.UpdateUserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;
import ru.cleverhause.users.entity.RoleEntity;
import ru.cleverhause.users.entity.UserEntity;
import ru.cleverhause.users.exceptions.UserNotFoundException;
import ru.cleverhause.users.repository.RoleDao;
import ru.cleverhause.users.repository.UserDao;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserInfoResponse> users() {
        return userDao.findAll().stream()
                .map(user -> UserInfoResponse.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(null)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public UserInfoResponse userInfo(@NotNull String username) {
        Optional<UserEntity> savedUser = userDao.findByUsername(username);
        return savedUser.map(u -> UserInfoResponse.builder()
                .userId(u.getId())
                .username(u.getUsername())
                .build())
                .orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.MSG_PATTERN, username)));
    }

    @Override
    public UserDetails userDetails(String username) {
//        Optional<UserEntity> savedUser = userDao.findByUsername(username);
        return User.withUsername(username)
                .password("password")
                .accountExpired(false)
                .accountLocked(false)
                .authorities(new SimpleGrantedAuthority("USER"))
                .build();
    }

    @Override
    public UserInfoResponse addUser(@NotNull AddUserRequest userRequest) {
        UserEntity newUser = new UserEntity();
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setUsername(userRequest.getUsername());
        newUser.setEmail(userRequest.getEmail());
        RoleEntity role = roleDao.findByRolename(Role.ROLE_USER.name()).orElse(new RoleEntity());
        newUser.setRoles(Set.of(role));
        UserEntity savedUser = userDao.save(newUser);
        userDao.flush();
        return UserInfoResponse.builder()
                .userId(savedUser.getId())
                .username(String.valueOf(savedUser.getUsername()))
                .email(String.valueOf(savedUser.getEmail()))
                .build();
    }

    @Override
    public boolean updateUser(UpdateUserRequest userRequest) {
        Optional<UserEntity> updatedUser = userDao.findByUsername(userRequest.getUsername());
        return updatedUser.map(uu -> updateUser(uu, userRequest)).orElse(false);
    }

    private boolean updateUser(@NotNull UserEntity updatedUser, @NotNull UpdateUserRequest userRequest) {
        boolean needUpdate = false;
        if (ObjectUtils.isNotEmpty(userRequest.getPassword())
                && !passwordEncoder.matches(userRequest.getPassword(), updatedUser.getPassword())) {
            updatedUser.setUsername(passwordEncoder.encode(userRequest.getPassword()));
            needUpdate = true;
        }
        if (ObjectUtils.isNotEmpty(userRequest.getUsername())
                && !userRequest.getUsername().equals(updatedUser.getUsername())) {
            updatedUser.setUsername(userRequest.getEmail());
            needUpdate = true;
        }
        if (needUpdate) {
            userDao.save(updatedUser);
        }
        return needUpdate;
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteById(userId);
    }
}
