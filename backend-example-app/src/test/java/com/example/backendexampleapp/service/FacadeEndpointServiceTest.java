package com.example.backendexampleapp.service;

import com.google.gson.Gson;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FacadeEndpointServiceTest {

    @Test
    @DisplayName("Given a valid refresh token user logouts")
    void userLogoutsWithValidRefreshToken() {
        FacadeEndpointService facadeEndpointService = new FacadeEndpointService();
        String token =  facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String refreshToken = jsonToken.get("refresh_token").toString();
        HttpStatus httpStatus = facadeEndpointService.logout(refreshToken).getStatusCode();
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    @DisplayName("Given valid user data retrieve the token")
    void getTokenAfterGivingValidData() {
        FacadeEndpointService facadeEndpointService = new FacadeEndpointService();
        HttpStatus httpStatus = facadeEndpointService.getToken("mate", "mate").getStatusCode();
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    @DisplayName("Given a valid user token receive token data")
    void getIntrospect() {
        FacadeEndpointService facadeEndpointService = new FacadeEndpointService();
        String token =  facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String accessToken = jsonToken.get("access_token").toString();
        HttpStatus httpStatus = facadeEndpointService.getIntrospect(accessToken).getStatusCode();
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    @DisplayName("Given a valid access token user receives user info")
    void getUserInfo() {
        FacadeEndpointService facadeEndpointService = new FacadeEndpointService();
        String token =  facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String acessToken = jsonToken.get("access_token").toString();
        HttpStatus httpStatus = facadeEndpointService.getUserInfo(acessToken).getStatusCode();
        assertEquals(HttpStatus.OK, httpStatus);
    }
}