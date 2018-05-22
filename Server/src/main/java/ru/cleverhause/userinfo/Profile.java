package ru.cleverhause.userinfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.context.annotation.SessionScope;
import ru.cleverhause.model.User;

import java.sql.Timestamp;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 12/5/2017.
 */
@SessionScope
@NoArgsConstructor
public class Profile {
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private Timestamp loginTime;


}
