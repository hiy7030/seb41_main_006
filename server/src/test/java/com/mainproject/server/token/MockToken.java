package com.mainproject.server.token;

import com.mainproject.server.auth.JwtTokenizer;
import org.springframework.http.HttpHeaders;

import java.util.*;

public class MockToken {
    private static final String AUTHORIZATION = "Authorization";
    private static final String REFRESH_TOKEN = "RefreshToken";

    private static JwtTokenizer jwtTokenizer = new JwtTokenizer();
    private static String secretKey= "hyeri1234123412341234123412341234";
    private static String base64EncodedSecretKey= jwtTokenizer.encodeSecretKeyWithBase64(secretKey);


    public static String createMockAccessToke() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("username", "test@gmail.com");
        claims.put("roles", List.of("USER"));

        String subject = "test access Token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String Token = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        String accessToken = "Bearer " + Token;
        return accessToken;
    }

    public static HttpHeaders getMockToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, createMockAccessToke());
        return headers;
    }
}
