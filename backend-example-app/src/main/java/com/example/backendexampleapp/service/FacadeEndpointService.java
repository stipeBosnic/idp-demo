package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.TokenData;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class FacadeEndpointService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String logout(String refreshToken) {
        TokenData data = TokenData.createRefresherToken(refreshToken);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/logout", request, String.class);
    }
    public String getToken(String username, String password) {
        TokenData data = new TokenData(username, password);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/token", request, String.class);
    }
    public String getIntrospect(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/introspect", request, String.class);
    }
    public String getUserInfo(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/userinfo", request, String.class);
    }
}

