package ru.cleverhause.app.filters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import ru.cleverhause.app.dto.DeviceData;
import ru.cleverhause.app.dto.request.BoardRequestBody;
import ru.cleverhause.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/31/2018.
 */
public class BoardBasicPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private BoardRequestBody<DeviceData> body;

    public BoardBasicPreAuthenticationFilter() {
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String principal = StringUtils.EMPTY;
        if (retrieveBodyFromRequest(request)) {
            principal = this.body.getUsername();
        }

        return principal;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String credentials = StringUtils.EMPTY;
        if (this.body != null) {
            credentials = this.body.getPassword();
            this.body = null;
        }

        return credentials;
    }

    private boolean retrieveBodyFromRequest(HttpServletRequest request) {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            try {
                this.body = JsonUtil.fromInputStreamToBoardData(request.getInputStream());
            } catch (Exception e) {
                e.printStackTrace(); //TODO
            }
        }

        return body != null;
    }
}
