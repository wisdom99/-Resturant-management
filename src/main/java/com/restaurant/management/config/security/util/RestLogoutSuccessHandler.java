package com.restaurant.management.config.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        String message = "{" +
                "\n\"userId\":\"" + "N/A" + "\","+
                "\n\"message\":\"Successfully logged out\"," +
                "\n\"authenticated\":false" +
                "\n}";
        response.getWriter().write(message);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
