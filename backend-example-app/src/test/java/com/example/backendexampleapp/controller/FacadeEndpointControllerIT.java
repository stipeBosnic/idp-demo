package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.service.FacadeEndpointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacadeEndpointController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class FacadeEndpointControllerIT {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    FacadeEndpointService facadeEndpointService;

    @MockBean
    RestTemplate restTemplate;

    @SpyBean
    FacadeEndpointController facadeEndpointController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(facadeEndpointService, "facadeTokenUrl", "http://localhost:8090/token");
        ReflectionTestUtils.setField(facadeEndpointService, "facadeLogoutUrl", "http://localhost:8090/logout");
        mockMvc = MockMvcBuilders.standaloneSetup(facadeEndpointController)
                .build();
    }

    @Test
    @DisplayName("Given valid username and password receive the token")
    void getToken() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).body("validToken");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .param("username", "valid")
                        .param("password", "valid"))
                        .andExpect(status().isOk())
                        .andReturn();
        assertEquals(response.getBody(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Given invalid username and password response is 401 UNAUTHORIZED")
    void tryToGetTokenWithInvalidParams() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .param("username", "invalid")
                        .param("password", "invalid"))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given empty string as username and password response is 401 UNAUTHORIZED")
    void tryToGetTokenWithEmptyStringAsParams() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .param("username", "")
                        .param("password", ""))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given valid refresh token user logouts")
    void logout() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/logout")
                        .param("refreshToken", "validRefreshToken"))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given an invalid refresh token response is 400 BAD REQUEST")
    void tryToLogoutWithInvalidRefreshToken() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(get("/logout")
                        .param("refreshToken", "invalidRefreshToken"))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given an empty string as refresh token response is 400 BAD REQUEST")
    void tryToLogoutWithEmptyStringAsRefreshToken() throws Exception {
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(get("/logout")
                        .param("refreshToken", ""))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    void getIntrospect() {
    }

    @Test
    void getUserInfo() {
    }
}