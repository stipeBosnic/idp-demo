package com.example.idpfacade.service;

import com.example.idpfacade.model.TokenData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
public class EndpointService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${logout}")
    private String keycloakLogout;
    @Value("${user-info-uri}")
    private String keycloakUserInfo;
    @Value("${introspect-uri}")
    private String keycloakIntrospect;
    @Value("${token-uri}")
    private String keycloakToken;
    @Value(("${endpoints-uri}"))
    private String endpoints;
    @Value("${client-secret}")
    private String clientSecret;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${user}")
    private String username;
    @Value("${user-password}")
    private String userPassword;

    public void logout(TokenData tokenData) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", tokenData.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        restTemplate.postForObject(keycloakLogout, request, String.class);
    }

    public String getUserInfo(TokenData tokenData) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer "+tokenData.getToken());
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        return restTemplate.postForObject(keycloakUserInfo, request, String.class);
    }
    public String getIntrospect(TokenData tokenData) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("token", tokenData.getToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        return restTemplate.postForObject(keycloakIntrospect, request, String.class);
    }
    public String getToken(TokenData data) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", data.getUsername());
        map.add("password", data.getPassword());
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
        return restTemplate.postForObject(keycloakToken, request, String.class);
    }
}
