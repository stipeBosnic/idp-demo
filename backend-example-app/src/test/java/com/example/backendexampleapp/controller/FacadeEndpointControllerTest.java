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
    void tryToLogoutWithInvalidParam() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout("invalidRefreshToken")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout("invalidRefreshToken"));
    }
    @Test
    @DisplayName("When given an empty string as refresh token the answer is bad request")
    void tryToLogoutWithEmptyStringAsParam() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout("")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout(""));
    }
    @Test
    @DisplayName("When given a null refresh token the answer is bad request")
    void tryToLogoutWithNullAsParam() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakResponse");
        when(facadeEndpointService.logout(null)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout(null));
    }

    @Test
    @DisplayName("When given valid username and password receive the token")
    void getToken() {
        ResponseEntity<String> validToken = ResponseEntity.ok("validToken");
        when(facadeEndpointService.getToken("validName", "validPassword")).thenReturn(validToken);
        ResponseEntity<String> response = facadeEndpointController.getToken("validName", "validPassword");
        assertEquals(validToken, response);
    }
    @Test
    @DisplayName("When given an invalid username and password receive unauthorized")
    void getTokenWithInvalidParams() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken("invalidName", "invalidPassword")).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken("invalidName", "invalidPassword");
        assertEquals(unauthorized, response);
    }

    @Test
    @DisplayName("When given null username and password receive unauthorized")
    void getTokenWithNullParams() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken(null, null)).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken(null, null);
        assertEquals(unauthorized, response);
    }

    @Test
    @DisplayName("When given an empty string as username and password receive unauthorized")
    void getTokenWithEmptyStringAsParams() {
        ResponseEntity<String> unauthorized = (ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        when(facadeEndpointService.getToken("", "")).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(""));
        ResponseEntity<String> response = facadeEndpointController.getToken("", "");
        assertEquals(unauthorized, response);
    }

    @Test
    void getIntrospect() {
    }

    @Test
    void getUserInfo() {
    }
}