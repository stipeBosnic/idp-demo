package com.example.idpfacade.controller;

import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(EndpointController.class)
public class EndpointControllerTest {

    @SpyBean
    EndpointController endpointController;

    @MockBean
    EndpointService endpointService;

    @Test
    @DisplayName("Given valid params receive the token")
    void getToken() {
        TokenData validInfo = new TokenData("validUsername", "validPassword");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("validToken");
        when(endpointService.getToken(validInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(validInfo));
    }
    @Test
    @DisplayName("Given invalid params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithInvalidParams() {
        TokenData invalidInfo = new TokenData("invalidUsername", "invalidPassword");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(invalidInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(invalidInfo));
    }

    @Test
    @DisplayName("Given empty string as params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithEmptyStringAsParams() {
        TokenData emptyInfo = new TokenData("", "");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(emptyInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(emptyInfo));
    }

    @Test
    @DisplayName("Given null params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithNullParams() {
        TokenData nullInfo = new TokenData(null, null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(endpointService.getToken(nullInfo)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(nullInfo));
    }

    @Test
    @DisplayName("Given a valid refresh Token user logouts")
    void logout() {
        TokenData tokenData = new TokenData("refreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an invalid refresh token to logout response is 400 bad request")
    void tryToLogoutWithInvalidRefreshToken() {
        TokenData tokenData = new TokenData("invalidRefreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an empty string as refresh token to logout response is 400 bad request")
    void tryToLogoutWithEmptyStringAsRefreshToken() {
        TokenData tokenData = new TokenData("");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given a null refresh token to logout response is 400 bad request")
    void tryToLogoutWithNullRefreshToken() {
        TokenData tokenData = new TokenData(null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(endpointService.logout(tokenData)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }
}
