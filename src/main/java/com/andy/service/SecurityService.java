package com.andy.service;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class SecurityService {
    private final Map<String,String> usernameAndPasswordDB;

    public SecurityService() {
        this.usernameAndPasswordDB = Map.of("andy", "password");
    }

    public boolean isValidAuth(String token) {
        String[] usernameAndPassword = new String(Base64.getDecoder().decode(token)).split(":");
        if (usernameAndPassword.length != 2) {
             throw new SecurityException("Invalid authorization header");
        }
        String username = usernameAndPassword[0];
        String password = usernameAndPassword[1];
        String validPassword = this.usernameAndPasswordDB.get(username);

        return validPassword != null && validPassword.equals(password);
    }

}
