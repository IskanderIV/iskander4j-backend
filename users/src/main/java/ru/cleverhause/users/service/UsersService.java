package ru.cleverhause.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.cleverhause.users.dto.request.AddUserRequest;
import ru.cleverhause.users.dto.request.UpdateUserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;

import java.util.List;

public interface UsersService {

    List<UserInfoResponse> users();

    UserInfoResponse userInfo(String user);

    UserDetails userDetails(String user);

    UserInfoResponse addUser(AddUserRequest userRequest);

    boolean updateUser(UpdateUserRequest userRequest);

    void deleteUser(Long userId);
}
