package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.TokenData;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class FacadeEndpointController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/logout")
    public String logout(@RequestParam String refreshToken) {
        TokenData data = TokenData.createRefresherToken(refreshToken);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/userlogout", request, String.class);
    }

    @PostMapping("/token")
    public String getToken(@RequestParam String username, @RequestParam String password, @RequestParam String clientId, @RequestParam String clientSecret) {
        TokenData data = new TokenData(username, password, clientId, clientSecret);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/gettoken", request, String.class);
    }
    @PostMapping(path = "/introspect")
    public String  getIntrospect(@RequestParam String clientId, @RequestParam String clientSecret, @RequestParam String token) {
        TokenData data = new TokenData(clientId, clientSecret, token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/getintrospect", request, String.class);
    }
    @GetMapping(path = "/userinfo")
    public String getUserInfo(@RequestParam String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        return restTemplate.postForObject("http://localhost:8090/getuserinfo", request, String.class);
    }
}
