package com.example.demo.security;

public class SecurityConstants {
    public static final String SECRET = "ggksddaeobv6066dsbks";
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
}
