package com.hoodie.otti.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hoodie.otti.util.login.JwtTokenProvider;

@Service
public class SomeService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String generateToken(String userEmail) {
        String token = jwtTokenProvider.createToken(userEmail);
        return token;
    }

    public boolean validateToken(String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        return isValid;
    }

    public String getUserEmailFromToken(String token) {
        String userEmail = jwtTokenProvider.getUserEmailFromToken(token);
        return userEmail;
    }
}

