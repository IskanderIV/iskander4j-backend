package ru.cleverhause.users.service;

import ru.cleverhause.users.dto.request.UserInfoRequest;
import ru.cleverhause.users.dto.request.UserRequest;
import ru.cleverhause.users.dto.response.UserInfoResponse;

public interface UsersService {

    UserInfoResponse userInfo(UserInfoRequest userInfoRequest);

    UserInfoResponse addUser(UserRequest userRequest);

    boolean updateUser(UserRequest userRequest);

    UserInfoResponse deleteUser(String userId);
}
