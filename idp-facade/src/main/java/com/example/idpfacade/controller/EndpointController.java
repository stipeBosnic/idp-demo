package com.example.idpfacade.controller;

import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
@RestController
public class EndpointController {

    @Autowired
    EndpointService endpointService;

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

    @PostMapping("/logout")
    public void logout(@RequestBody TokenData tokenData) {
        endpointService.logout(tokenData);
    }

    @PostMapping("/userinfo")
    public String getUserInfo(@RequestBody TokenData tokenData) {
        return endpointService.getUserInfo(tokenData);
    }

    @PostMapping("/introspect")
    public String getIntrospect(@RequestBody TokenData tokenData) {
        return endpointService.getIntrospect(tokenData);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody TokenData data) {
        return endpointService.getToken(data);
    }
}

