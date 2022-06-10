package com.example.idpfacade.controller;

import com.example.idpfacade.dto.TokenData;
import com.example.idpfacade.service.EndpointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(EndpointController.class)
class EndpointControllerTest {

    @SpyBean
    EndpointController endpointController;

    @MockBean
    EndpointService endpointService;

    @Test
    @DisplayName("Given valid params receive the token")
    void getTokenTest() {
        TokenData validInfo = new TokenData("validUsername", "validPassword");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("validToken");
        when(endpointService.getToken(validInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(validInfo));
    }

    @Test
    @DisplayName("Given invalid params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithInvalidParamsTest() {
        TokenData invalidInfo = new TokenData("invalidUsername", "invalidPassword");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(invalidInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(invalidInfo));
    }

    @Test
    @DisplayName("Given empty string as params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithEmptyStringAsParamsTest() {
        TokenData emptyInfo = new TokenData("", "");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(emptyInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(emptyInfo));
    }

    @Test
    @DisplayName("Given null params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithNullParamsTest() {
        TokenData nullInfo = new TokenData(null, null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(nullInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(nullInfo));
    }

    @Test
    @DisplayName("Given a valid refresh Token user logouts")
    void logoutTest() {
        TokenData tokenData = new TokenData("refreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an invalid refresh token to logout response is 400 bad request")
    void tryToLogoutWithInvalidRefreshTokenTest() {
        TokenData tokenData = new TokenData("invalidRefreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an empty string as refresh token to logout response is 400 bad request")
    void tryToLogoutWithEmptyStringAsRefreshTokenTest() {
        TokenData tokenData = new TokenData("");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given a null refresh token to logout response is 400 bad request")
    void tryToLogoutWithNullRefreshTokenTest() {
        TokenData tokenData = new TokenData(null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given a valid token receive userinfo")
    void getUserInfoTest() {
        TokenData tokenData = new TokenData("validToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("userinfo");
        when(endpointService.getUserinfo(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given an invalid token to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserinfoWithInvalidTokenTest() {
        TokenData tokenData = new TokenData("invalidToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getUserinfo(any())).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given a valid token receive introspect data")
    void getIntrospectTest() {
        TokenData tokenData = new TokenData("validToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("introspect");
        when(endpointService.getIntrospect(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getIntrospect(tokenData));
    }

    @Test
    @DisplayName("Given an invalid token to the introspect endpoint receive keycloak message")
    void tryToGetIntrospectWithInvalidTokenTest() {
        TokenData tokenData = new TokenData("invalidToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("keycloakMessage");
        when(endpointService.getUserinfo(any())).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }
}
