package ru.cleverhause.app.config.root;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.cleverhause.app.config.board.BoardWebSecurityConfig;
import ru.cleverhause.app.config.front.FrontWebSecurityConfig;

/**
 * Security configuration class
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @version v.1.0.0
 * @date 3/5/2018.
 */
@Configuration
@Import(value = {BoardWebSecurityConfig.class, FrontWebSecurityConfig.class})
public class CommonSecurityConfig {
}
