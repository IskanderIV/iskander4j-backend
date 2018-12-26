package ru.cleverhause.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.SessionScope;

import java.sql.Timestamp;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/5/2017.
 */
@SessionScope
@AllArgsConstructor
@Getter
@Setter
public class Profile {
    private String username;
    private String sessionId;
    private boolean isExpired = true;
    private boolean isLogged = false;
    private String address;
    private String phoneNumber;
    private Timestamp loginTime;

    public Profile() {
    }
}
