package com.example.idpfacade.service;

import com.example.idpfacade.model.TokenData;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.authorization.client.util.Http;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.org.yaml.snakeyaml.tokens.Token;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class EndpointServiceTest {

    @SpyBean
    EndpointService endpointService;
    @MockBean
    RestTemplate restTemplate;


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(endpointService, "keycloakLogout", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/logout");
        ReflectionTestUtils.setField(endpointService, "keycloakUserInfo", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/userinfo");
        ReflectionTestUtils.setField(endpointService, "keycloakIntrospect", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/token/introspect");
        ReflectionTestUtils.setField(endpointService, "keycloakToken", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/token");
        ReflectionTestUtils.setField(endpointService, "clientSecret", "NfPFBF9Cjqfpd7GcOH6uVVQ9EevZHfBf");
        ReflectionTestUtils.setField(endpointService, "clientId", "idp-facade");
        ReflectionTestUtils.setField(endpointService, "clientId", "idp-facade");
    }

    @Test
    @DisplayName("When given valid params receive the token")
    void getTokenWithValidParamsTest() {
        TokenData validTokenData = new TokenData("valid", "valid");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("validToken");
        Mockito.when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.getToken(validTokenData));
    }

    @Test
    @DisplayName("When given invalid params response is 401 Unauthorized")
    void tryToGetTokenWithInvalidParamsTest() {
        TokenData validTokenData = new TokenData("invalid", "invalid");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        Mockito.when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.getToken(validTokenData));
    }

    @Test
    @DisplayName("When given null params response is 401 Unauthorized")
    void tryToGetTokenWithNullParamsTest() {
        TokenData nullTokenData = new TokenData(null, null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        Mockito.when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.getToken(nullTokenData));
    }

    @Test
    @DisplayName("When given empty string as param response is 401 Unauthorized")
    void tryToGetTokenEmptyStringsAsParamTest() {
        TokenData emptyTokenData = new TokenData("", "");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        Mockito.when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.getToken(emptyTokenData));
    }

    @Test
    @DisplayName("When given a valid refresh token user logouts")
    void logoutTest() {
        TokenData validTokenData = new TokenData("refreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.logout(validTokenData));
    }
    @Test
    @DisplayName("When given an invalid refresh token response is 400 bad request")
    void tryToLogoutWithInvalidRefreshTokenTest() {
        TokenData validTokenData = new TokenData("invalidRefreshToken");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.logout(validTokenData));
    }
    @Test
    @DisplayName("When given an empty string as refresh token response is 400 bad request")
    void tryToLogoutWithEmptyStringAsRefreshTokenTest() {
        TokenData validTokenData = new TokenData("");
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.logout(validTokenData));
    }
    @Test
    @DisplayName("When given null refresh token response is 400 bad request")
    void tryToLogoutWithNullRefreshTokenTest() {
        TokenData validTokenData = new TokenData(null);
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, endpointService.logout(validTokenData));
    }
}
