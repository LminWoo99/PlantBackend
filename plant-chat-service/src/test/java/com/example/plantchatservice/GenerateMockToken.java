package com.example.plantchatservice;

import org.springframework.http.HttpHeaders;

public abstract class GenerateMockToken {

    public static HttpHeaders getToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String token = "\"access token\"";
        httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
        return httpHeaders;
    }
}
