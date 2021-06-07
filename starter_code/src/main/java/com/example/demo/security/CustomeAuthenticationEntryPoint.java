package com.example.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomeAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String headerValue;
    private static final Logger logger = LoggerFactory.getLogger("splunkLogger");

    public CustomeAuthenticationEntryPoint(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setHeader("Authenticate", this.headerValue);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                authException.getMessage());

        logger.info("Failure: Sign In Bad Credentials");
    }
}