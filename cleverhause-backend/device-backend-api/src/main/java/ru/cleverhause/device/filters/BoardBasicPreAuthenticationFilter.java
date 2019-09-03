package ru.cleverhause.device.filters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import ru.cleverhause.device.dto.request.BoardRequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by
 *
 * @author Aleksandr_Ivanov1
 * @date 7/31/2018.
 */
public class BoardBasicPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private BoardRequestBody<?> body;

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
                this.body = fromInputStreamToBoardData(request.getInputStream());
            } catch (Exception e) {
                e.printStackTrace(); //TODO
            }
        }

        return body != null;
    }

    private <T extends Serializable> BoardRequestBody<T> fromInputStreamToBoardData(InputStream src) throws IOException {
        return new ObjectMapper().readValue(src, new TypeReference<BoardRequestBody<T>>() {
        });
    }
}
