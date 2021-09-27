package com.restaurant.management.config.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.management.config.security.JsonLoginRequest;
import com.restaurant.management.model.User;
import com.restaurant.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;



public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger logger = LoggerFactory.getLogger(JsonAuthenticationFilter.class.getSimpleName());
    private boolean postOnly = true;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        logger.debug("Initializing a json login authentication context");

        // TODO remember to as verify that the requesting IP address is trusted
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UsernamePasswordAuthenticationToken authRequest = this.getUserNamePasswordAuthenticationToken(request);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getUserNamePasswordAuthenticationToken(HttpServletRequest request) {

        logger.debug("Retrieving user logging credentials...");

        JsonLoginRequest jsonLoginRequest = getAuthBody(request);
		logger.debug("Done retrieving user login credentials!");

		User user = userRepository.findByUsername(jsonLoginRequest.getUserId());

		if (!(passwordEncoder.matches(jsonLoginRequest.getSecret(), user.getPassword()))){
			throw new AccessDeniedException("Username/password incorrect");
		}

        logger.debug("Attempting to create a security context for user ID: {}", jsonLoginRequest.getUserId());

        return new UsernamePasswordAuthenticationToken(jsonLoginRequest.getUserId(), jsonLoginRequest.getSecret());
    }

    private JsonLoginRequest getAuthBody(HttpServletRequest request){

        StringBuffer sb = new StringBuffer();
        BufferedReader bufferedReader = null;
        String content = "";

        JsonLoginRequest jsonLoginRequest = null;

        try {
            bufferedReader =  request.getReader();
            char[] charBuffer = new char[128];
            int bytesRead;
            while ( (bytesRead = bufferedReader.read(charBuffer)) != -1 ) {
                sb.append(charBuffer, 0, bytesRead);
            }
            content = sb.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            try{
                jsonLoginRequest = objectMapper.readValue(content, JsonLoginRequest.class);
            }catch(Throwable t){
                throw new IOException(t.getMessage(), t);
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return jsonLoginRequest;
    }

}

