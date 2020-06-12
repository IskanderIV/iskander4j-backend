package ru.cleverhause.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cleverhause.users.dto.request.UserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;
import ru.cleverhause.users.entity.User;
import ru.cleverhause.users.repository.RoleDao;
import ru.cleverhause.users.repository.UserDao;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

import static ru.cleverhause.users.entity.Role.DEF_ROLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoResponse userInfo(@NotNull String user) {
        Optional<User> savedUser = userDao.findUser(Long.valueOf(user), user);
        return savedUser.map(u -> UserInfoResponse.builder()
                .userId(u.getId())
                .username(u.getUsername())
                .build())
                .orElse(UserInfoResponse.builder().build());
    }

    @Override
    public UserInfoResponse addUser(@NotNull UserRequest userRequest) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setRoles(Set.of(roleDao.findByRolename(DEF_ROLE)));
        User savedUser = userDao.save(newUser);
        return UserInfoResponse.builder()
                .userId(savedUser.getId())
                .username(String.valueOf(savedUser.getUsername()))
                .email(String.valueOf(savedUser.getId()))
                .build();
    }

    @Override
    public boolean updateUser(@NotNull UserRequest userRequest) {
        Optional<User> updatedUser = userDao.findUser(userRequest.getUserId(), userRequest.getUsername());
        return updatedUser.map(uu -> updateUser(uu, userRequest)).orElse(false);
    }

    private boolean updateUser(@NotNull User updatedUser, @NotNull UserRequest userRequest) {
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
    public UserInfoResponse deleteUser(String userId) {
        return null;
    }
}
