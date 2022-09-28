package com.example.finalexam.token;

public class JWTConfig {

    public static String secret = "BvPHGM8C0ia4uOuxxqPD5DTbWC9F9TWvPStp3pb7ARo0oK2mJ3pd3YG4lxA9i8bj6OTbadwezxgeEByY";

    public static String tokenHeader = "Authorization";

    public static String tokenPrefix = "Bearer";

    public static Integer expiration = 7 * 24 * 60 * 60 * 1000;

    public static Integer refreshTime = 7 * 24 * 60 * 60 * 1000;

    public static String antMatchers = "/api/v1/user/login/**,/api/auth/check-token/**";

    public void setExpiration(Integer expiration) {
        this.expiration = expiration * 1000;
    }

    public void setRefreshTime(Integer refreshTime) {
        this.refreshTime = refreshTime * 24 * 60 * 60 * 1000;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix + " ";
    }

    public void setAntMatchers(String antMatchers) {
        this.antMatchers = antMatchers;
    }

}
