package com.example.SecureLoginPUC.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public String createToken(String email) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, email);
        return token;
    }

    public String getEmail(String token) {
        return token == null ? null : tokens.get(token);
    }

    public void invalidate(String token) {
        tokens.remove(token);
    }
}
