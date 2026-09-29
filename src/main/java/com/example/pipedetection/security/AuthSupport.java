package com.example.pipedetection.security;

import com.example.pipedetection.entity.AppUser;
import jakarta.servlet.http.HttpSession;

public final class AuthSupport {
    public static final String USER_SESSION_KEY = "AUTH_USER";
    private AuthSupport() {}

    public static AppUser currentUser(HttpSession session) {
        Object value = session == null ? null : session.getAttribute(USER_SESSION_KEY);
        return value instanceof AppUser ? (AppUser) value : null;
    }
}
