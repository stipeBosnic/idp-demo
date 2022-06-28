package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.dto.TokenData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class FacadeEndpointController {

    @Value("${facade.userinfo-url}")
    private String facadeUserinfoUrl;

    @Value("${facade.introspect-url}")
    private String facadeIntrospectUrl;

    @Value("${facade.logout-url}")
    private String facadeLogoutUrl;

    @Value("${facade.token-url}")
    private String facadeTokenUrl;

    private final RestTemplate restTemplate;

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        TokenData data = TokenData.createRefresherToken(refreshToken);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeLogoutUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam String username, @RequestParam String password) {
        TokenData data = new TokenData(username, password);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeTokenUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/introspect")
    public ResponseEntity<String> getIntrospect(@RequestParam String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeIntrospectUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }

    }

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestParam String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            return restTemplate.exchange(facadeUserinfoUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
