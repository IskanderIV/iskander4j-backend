package ru.cleverhause.users.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public static final String MSG_PATTERN = "User with user=%s not found";

    public UserNotFoundException(String message) {
        super(message);
    }
}
