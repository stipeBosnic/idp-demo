package com.example.idpfacade.controller;

import com.example.idpfacade.dto.TokenData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class EndpointController {

    @Value("${logout}")
    private String keycloakLogout;
    @Value("${user-info-uri}")
    private String keycloakUserInfo;
    @Value("${introspect-uri}")
    private String keycloakIntrospect;
    @Value("${token-uri}")
    private String keycloakToken;
    @Value("${client-secret}")
    private String clientSecret;
    @Value("${keycloak.resource}")
    private String clientId;

    private final RestTemplate restTemplate;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenData tokenData) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", tokenData.getRefreshToken());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);

        try {
            return restTemplate.exchange(keycloakLogout, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestBody TokenData tokenData) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + tokenData.getToken());
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);

        try {
            return restTemplate.exchange(keycloakUserInfo, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/introspect")
    public ResponseEntity<String> getIntrospect(@RequestBody TokenData tokenData) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("token", tokenData.getToken());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);

        try {
            return restTemplate.exchange(keycloakIntrospect, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }

    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody TokenData data) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", data.getUsername());
        map.add("password", data.getPassword());
        map.add("client_id", clientId);
        map.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);

        try {
            return restTemplate.exchange(keycloakToken, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}
