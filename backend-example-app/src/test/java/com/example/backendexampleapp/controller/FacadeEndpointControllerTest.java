package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.service.FacadeEndpointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(FacadeEndpointController.class)
class FacadeEndpointControllerTest {

    @SpyBean
    FacadeEndpointController facadeEndpointController;

    @MockBean
    FacadeEndpointService facadeEndpointService;

    @Test
    @DisplayName("When given valid refresh token user logouts")
    void logout() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(204).body(null);
        when(facadeEndpointService.logout("validRefreshToken")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout("validRefreshToken"));
    }
    @Test
    @DisplayName("When given an invalid refresh token the answer is bad request")
    void tryToLogoutWithInvalidParamTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout("invalidRefreshToken")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout("invalidRefreshToken"));
    }
    @Test
    @DisplayName("When given an empty string as refresh token the answer is bad request")
    void tryToLogoutWithEmptyStringAsParamTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout("")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout(""));
    }
    @Test
    @DisplayName("When given a null refresh token the answer is bad request")
    void tryToLogoutWithNullAsParamTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout(null)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout(null));
    }

    @Test
    @DisplayName("When given valid username and password receive the token")
    void getTokenTest() {
        ResponseEntity<String> validToken = ResponseEntity.ok("validToken");
        when(facadeEndpointService.getToken("validName", "validPassword")).thenReturn(validToken);
        ResponseEntity<String> response = facadeEndpointController.getToken("validName", "validPassword");
        assertEquals(validToken, response);
    }
    @Test
    @DisplayName("When given an invalid username and password receive unauthorized")
    void getTokenWithInvalidParamsTest() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken("invalidName", "invalidPassword")).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken("invalidName", "invalidPassword");
        assertEquals(unauthorized, response);
    }

    @Test
    @DisplayName("When given null username and password receive unauthorized")
    void getTokenWithNullParamsTest() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken(null, null)).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken(null, null);
        assertEquals(unauthorized, response);
    }

    @Test
    @DisplayName("When given an empty string as username and password receive unauthorized")
    void getTokenWithEmptyStringAsParamsTest() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken("", "")).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken("", "");
        assertEquals(unauthorized, response);
    }

    @Test
    @DisplayName("When given a valid token to the introspect endpoint receive the introspect info")
    void getIntrospectTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.OK).body("validInfo"));
        when(facadeEndpointService.getIntrospect("validToken")).thenReturn(ResponseEntity.status(HttpStatus.OK).body("validInfo"));
        ResponseEntity<String> response = facadeEndpointController.getIntrospect("validToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("When given an invalid token to the introspect endpoint receive the keycloak message")
    void tryToGetIntrospectWithInvalidTokenTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.OK).body("keycloakMessage"));
        when(facadeEndpointService.getIntrospect(anyString())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("keycloakMessage"));
        ResponseEntity<String> response = facadeEndpointController.getIntrospect("invalidToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("When given a valid token to the userinfo endpoint receive userinfo")
    void getUserInfoTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.OK).body("validInfo"));
        when(facadeEndpointService.getUserInfo("validToken")).thenReturn(ResponseEntity.status(HttpStatus.OK).body("validInfo"));
        ResponseEntity<String> response = facadeEndpointController.getUserInfo("validToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("When given a invalid token to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserInfoWithInvalidTokenTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getUserInfo(any())).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getUserInfo("invalidToken");
        assertEquals(expectedResponse, response);
    }
}