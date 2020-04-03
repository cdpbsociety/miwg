package com.andy.controller;

import com.andy.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityHandler implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final SecurityService securityService;

    @Autowired
    public SecurityHandler(SecurityService securityService) {
        this.securityService = securityService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isAuthenticatedUri(request)) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null) {
                throw new SecurityException("Authorization header not provided");
            }

            String[] basicToken = authHeader.split("(?i)Basic "); // format: "Basic A4G6J8j4jkhlkj45lk645jlk54lk"
            if (basicToken.length != 2) {
                throw new SecurityException("Basic authorization header not provided");
            }

            String token = basicToken[1];
            boolean valid = securityService.isValidAuth(token);
            if (!valid) {
                throw new SecurityException("Username and/or password is not valid");
            }
        }
        return true;
    }

    // For this code test it's only one uri but this could be used for multiple ones.
    private boolean isAuthenticatedUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (StringUtils.startsWith(uri, "/items/buy")) {
            log.debug("{} will be authenticated", uri);
            return true;
        }
        log.debug("{} is public", uri);
        return false;
    }
}
