package ru.cleverhause.api.service.security;

/**
 * Service for security.
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v1.0.0
 * @date 3/4/2018
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
