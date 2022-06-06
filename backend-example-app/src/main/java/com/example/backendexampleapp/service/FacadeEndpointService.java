package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.TokenData;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FacadeEndpointService {


    @Value("${facade.userinfo-url}")
    private String facadeUserinfoUrl;

    @Value("${facade.introspect-url}")
    private String facadeIntrospectUrl;
    @Value("${facade.logout-url}")
    private String facadeLogoutUrl;

    @Value("${facade.token-url}")
    private String facadeTokenUrl;
    private final RestTemplate restTemplate;

    public ResponseEntity<String> logout(String refreshToken) {
        TokenData data = TokenData.createRefresherToken(refreshToken);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeLogoutUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> getToken(String username, String password) {
        TokenData data = new TokenData(username, password);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeTokenUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> getIntrospect(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeIntrospectUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> getUserInfo(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeUserinfoUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}

