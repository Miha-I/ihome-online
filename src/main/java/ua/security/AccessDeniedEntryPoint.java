package ua.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
public class AccessDeniedEntryPoint implements AuthenticationEntryPoint {

    public AccessDeniedEntryPoint(String accessDeniedPage){
        Assert.notNull(accessDeniedPage, "accessDeniedPage cannot be null");
        this.accessDeniedPage = accessDeniedPage;
    }

    private Logger logger = LoggerFactory.getLogger(AccessDeniedEntryPoint.class);

    private String accessDeniedPage;

    private boolean useForward = false;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (useForward) {
                logger.debug("Server side forward to: " + accessDeniedPage);
                request.getRequestDispatcher(accessDeniedPage).forward(request, response);
                return;
            }
            logger.debug("Server side redirect to: " + accessDeniedPage);
            response.sendRedirect(accessDeniedPage);
        }
    }

    public void setAccessDeniedPage(String accessDeniedPage){
        this.accessDeniedPage = accessDeniedPage;
    }

    public String getAccessDeniedPage() {
        return accessDeniedPage;
    }

    public boolean isUseForward() {
        return useForward;
    }

    public void setUseForward(boolean useForward) {
        this.useForward = useForward;
    }
}
