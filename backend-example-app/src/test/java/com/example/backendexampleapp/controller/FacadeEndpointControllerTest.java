package com.example.backendexampleapp.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(FacadeEndpointController.class)
class FacadeEndpointControllerTest {

    @SpyBean
    FacadeEndpointController facadeEndpointController;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(facadeEndpointController, "facadeTokenUrl", "http://localhost:8090/token");
        ReflectionTestUtils.setField(facadeEndpointController, "facadeIntrospectUrl", "http://localhost:8090/introspect");
        ReflectionTestUtils.setField(facadeEndpointController, "facadeUserinfoUrl", "http://localhost:8090/userinfo");
        ReflectionTestUtils.setField(facadeEndpointController, "facadeLogoutUrl", "http://localhost:8090/logout");
    }

    @Test
    @DisplayName("When given valid refresh token user logouts")
    void logout() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(204).body(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.logout("validRefreshToken"));
    }
    @Test
    @DisplayName("When given an invalid refresh token the answer is bad request")
    void tryToLogoutWithInvalidParamTest() {
        String message = "keycloakResponse";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakResponse"), facadeEndpointController.logout("invalidRefreshToken"));
    }
    @Test
    @DisplayName("When given an empty string as refresh token the answer is bad request")
    void tryToLogoutWithEmptyStringAsParamTest() {
        String message = "keycloakResponse";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakResponse"), facadeEndpointController.logout(""));
    }
    @Test
    @DisplayName("When given a null refresh token the answer is bad request")
    void tryToLogoutWithNullAsParamTest() {
        String message = "keycloakResponse";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body("keycloakResponse"), facadeEndpointController.logout(null));
    }

    @Test
    @DisplayName("When given valid username and password receive the token")
    void getTokenTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("validToken");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getToken("validUsernamne", "validPassword"));
    }

    @Test
    @DisplayName("When given an invalid username and password response is 401 unauthorized")
    void getTokenWithInvalidParamsTest() {
        RestClientResponseException exception = new RestClientResponseException("", 401, "UNAUTHORIZED", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), facadeEndpointController.getToken("invalidUsername", "invalidPassword"));
    }

    @Test
    @DisplayName("When given null username and password receive unauthorized")
    void getTokenWithNullParamsTest() {
        String message = "badRequestResponse";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(400).body(message), facadeEndpointController.getToken(null, null));
    }

    @Test
    @DisplayName("When given an empty string as username and password receive unauthorized")
    void getTokenWithEmptyStringAsParamsTest() {
        RestClientResponseException exception = new RestClientResponseException("", 401, "UNAUTHORIZED", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);
        assertEquals(ResponseEntity.status(401).body(""), facadeEndpointController.getToken("", ""));
    }

    @Test
    @DisplayName("When given a valid token to the introspect endpoint receive the introspect info")
    void getIntrospectTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("introspectInfo");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getIntrospect("validToken"));
    }

    @Test
    @DisplayName("When given an invalid token to the introspect endpoint receive the keycloak message")
    void tryToGetIntrospectWithInvalidTokenTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.OK).body("keycloakMessage"));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getIntrospect("invalidToken"));
    }

    @Test
    @DisplayName("When given an empty string as token to the introspect endpoint receive the keycloak message")
    void tryToGetIntrospectWithEmptyStringAsTokenTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.OK).body("keycloakMessage"));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getIntrospect(""));
    }

    @Test
    @DisplayName("When given a null token to the introspect endpoint receive the keycloak message")
    void tryToGetIntrospectWithNullTokenTest() {
        ResponseEntity<String> expectedResponse = (ResponseEntity.status(HttpStatus.BAD_REQUEST).body("message"));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getIntrospect(null));
    }


    @Test
    @DisplayName("When given a valid token to the userinfo endpoint receive userinfo")
    void getUserInfoTest() {
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.OK).body("userinfo");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        assertEquals(expectedResponse, facadeEndpointController.getUserInfo("validToken"));
    }

    @Test
    @DisplayName("When given a invalid token to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserInfoWithInvalidTokenTest() {
        RestClientResponseException exception = new RestClientResponseException("", 401, "UNAUTHORIZED", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);;
        assertEquals(ResponseEntity.status(401).body(""), facadeEndpointController.getUserInfo("invalidInfo"));
    }

    @Test
    @DisplayName("When given an empty string to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserInfoWithEmptyStringAsTokenTest() {
        RestClientResponseException exception = new RestClientResponseException("", 401, "UNAUTHORIZED", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);;
        assertEquals(ResponseEntity.status(401).body(""), facadeEndpointController.getUserInfo(""));
    }

    @Test
    @DisplayName("When given a null token to the userinfo endpoint receive 401 UNAUTHORIZED")
    void tryToGetUserInfoWithNullTokenTest() {
        String message = "badRequestResponse";
        RestClientResponseException exception = new RestClientResponseException("", 400, "BAD REQUEST", null, message.getBytes(StandardCharsets.UTF_8), null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(exception);;
        assertEquals(ResponseEntity.status(400).body("badRequestResponse"), facadeEndpointController.getUserInfo(null));
    }
}