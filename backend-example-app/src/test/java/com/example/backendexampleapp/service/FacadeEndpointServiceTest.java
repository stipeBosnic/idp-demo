package com.example.backendexampleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class FacadeEndpointServiceTest {

    @SpyBean
    FacadeEndpointService facadeEndpointService;
    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        facadeEndpointService = new FacadeEndpointService(restTemplate);
        ReflectionTestUtils.setField(facadeEndpointService, "facadeTokenUrl", "http://localhost:8090/token");
        ReflectionTestUtils.setField(facadeEndpointService, "facadeIntrospectUrl", "http://localhost:8090/introspect");
        ReflectionTestUtils.setField(facadeEndpointService, "facadeUserinfoUrl", "http://localhost:8090/userinfo");
        ReflectionTestUtils.setField(facadeEndpointService, "facadeLogoutUrl", "http://localhost:8090/logout");

    }

    @Test
    @DisplayName("Given a valid refresh token user logouts")
    void userLogoutsWithValidRefreshToken() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        ResponseEntity<String> response = facadeEndpointService.logout("refreshToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Given valid username and password receive the token")
    void getTokenAfterProvidingData() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("token");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        ResponseEntity<String> response = facadeEndpointService.getToken("test", "test");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Given a valid user token receive token data")
    void getIntrospect() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("introspect");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        ResponseEntity<String> response = facadeEndpointService.getIntrospect("validToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Given a valid access token user receives userinfo")
    void getUserInfo() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("userinfo");
        when(restTemplate.exchange(anyString(),eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        ResponseEntity<String> response = facadeEndpointService.getUserInfo("validToken");
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Given an invalid username and password response if 401 unauthorized")
    void getTokenAfterProvidingInvalidData() {
    }
}