package com.example.trainbuddy_server.util;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

    public static ResponseCookie createHttpOnlyCookie(String name, String value, long maxAgeMillis) {
        long maxAgeSeconds = maxAgeMillis / 1_000;
        return ResponseCookie.from(name, value)
                             .httpOnly(true)
                             .path("/")
                             .maxAge(maxAgeSeconds)
                             .build();
    }

    public static String getCookieValue(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        for (var c : req.getCookies()) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    public static ResponseCookie clearCookie(String name) {
        return ResponseCookie.from(name, "")
                             .httpOnly(true)
                             .path("/")
                             .maxAge(0)
                             .build();
    }
}
