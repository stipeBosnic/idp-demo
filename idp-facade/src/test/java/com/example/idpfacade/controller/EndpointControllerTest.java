package com.example.idpfacade.controller;

import com.example.idpfacade.dto.TokenData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(EndpointController.class)
class EndpointControllerTest {

    @SpyBean
    EndpointController endpointController;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(endpointController, "keycloakLogout", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/logout");
        ReflectionTestUtils.setField(endpointController, "keycloakUserInfo", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/userinfo");
        ReflectionTestUtils.setField(endpointController, "keycloakIntrospect", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/token/introspect");
        ReflectionTestUtils.setField(endpointController, "keycloakToken", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/token");
        ReflectionTestUtils.setField(endpointController, "clientSecret", "NfPFBF9Cjqfpd7GcOH6uVVQ9EevZHfBf");
        ReflectionTestUtils.setField(endpointController, "clientId", "idp-facade");
    }

    @Test
    @DisplayName("Given valid params receive the token")
    void getTokenTest() {
        TokenData validInfo = new TokenData("validUsername", "validPassword");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("validToken");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getToken(validInfo));
    }

    @Test
    @DisplayName("Given invalid params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithInvalidParamsTest() {
        TokenData invalidInfo = new TokenData("invalidUsername", "invalidPassword");
        RestClientResponseException exception = new RestClientResponseException("", 401, "Unauthorized", null, null, null);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), endpointController.getToken(invalidInfo));
    }

    @Test
    @DisplayName("Given empty string as params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithEmptyStringAsParamsTest() {
        TokenData emptyInfo = new TokenData("", "");
        RestClientResponseException exception = new RestClientResponseException("", 401, "Unauthorized", null, null, null);
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), endpointController.getToken(emptyInfo));
    }

    @Test
    @DisplayName("Given null params to receive the token, response is 401 unauthorized")
    void tryToGetTokenWithNullParamsTest() {
        TokenData nullInfo = new TokenData(null, null);
        RestClientResponseException exception = new RestClientResponseException("", 401, "Unauthorized", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), endpointController.getToken(nullInfo));
    }

    @Test
    @DisplayName("Given a valid refresh Token user logouts")
    void logoutTest() {
        TokenData tokenData = new TokenData("refreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an invalid refresh token to logout response is 400 bad request")
    void tryToLogoutWithInvalidRefreshTokenTest() {
        TokenData tokenData = new TokenData("invalidRefreshToken");
        String message = "keycloakMessage";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakMessage"), endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given an empty string as refresh token to logout response is 400 bad request")
    void tryToLogoutWithEmptyStringAsRefreshTokenTest() {
        TokenData tokenData = new TokenData("");
        String message = "keycloakMessage";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakMessage"), endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given a null refresh token to logout response is 400 bad request")
    void tryToLogoutWithNullRefreshTokenTest() {
        TokenData tokenData = new TokenData(null);
        String message = "keycloakMessage";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakMessage"), endpointController.logout(tokenData));
    }

    @Test
    @DisplayName("Given a valid token receive userinfo")
    void getUserInfoTest() {
        TokenData tokenData = new TokenData("validToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("userinfo");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given an invalid token to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserinfoWithInvalidTokenTest() {
        TokenData tokenData = new TokenData("invalidToken");
        RestClientResponseException exception = new RestClientResponseException("", 401, "UNAUTHORIZED", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given an empty string as token to the userinfo endpoint receive 400 BAD REQUEST")
    void tryToGetUserinfoWithEmptyStringAsTokenTest() {
        TokenData tokenData = new TokenData("");
        String message = "keycloakMessage";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakMessage"), endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given a null token to the userinfo endpoint receive 400 BAD REQUEST")
    void tryToGetUserinfoWithNullTokenTest() {
        TokenData tokenData = new TokenData(null);
        String message = "keycloakMessage";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakMessage"), endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given a valid token receive introspect data")
    void getIntrospectTest() {
        TokenData tokenData = new TokenData("validToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("introspect");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getIntrospect(tokenData));
    }

    @Test
    @DisplayName("Given an invalid token to the introspect endpoint receive keycloak message")
    void tryToGetIntrospectWithInvalidTokenTest() {
        TokenData tokenData = new TokenData("invalidToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given an empty string to the introspect endpoint receive keycloak message")
    void tryToGetIntrospectWithEmptyStringAsTokenTest() {
        TokenData tokenData = new TokenData("");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }

    @Test
    @DisplayName("Given an invalid token to the introspect endpoint receive keycloak message")
    void tryToGetIntrospectWithNullTokenTest() {
        TokenData tokenData = new TokenData(null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointController.getUserInfo(tokenData));
    }
}
