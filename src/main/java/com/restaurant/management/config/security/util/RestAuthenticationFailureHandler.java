package com.restaurant.management.config.security.util;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = "{" +
                "\n\"userId\":\"" + "N/A" + "\","+
                "\n\"message\":\"" + exception.getMessage() +"\"," +
                "\n\"authenticated\":false" +
                "\n}";
        response.getWriter().write(message);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
