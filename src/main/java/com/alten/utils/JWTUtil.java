package com.alten.utils;

public class JWTUtil {
    public static final String AUTH_HEADER = "Authorization";
    public static final String SECRET = "Password123";
    public static final String PREFIX = "Bearer ";
    public static final long EXPIRE_ACCESS_TOKEN = 5*60*1000; // 1 minute
    public static final long EXPIRE_REFRESH_TOKEN = 60*60*1000;// 1 houre
    public static final String TOKEN = "/token";
    public static final String REFRESH_TOKEN = "/refreshToken";
}
