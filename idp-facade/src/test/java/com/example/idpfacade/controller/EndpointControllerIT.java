package com.example.idpfacade.controller;


import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
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
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EndpointController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class EndpointControllerIT {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    EndpointController endpointController;

    @SpyBean
    EndpointService endpointService;
    @MockBean
    RestTemplate restTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(endpointService, "clientId", "idp-facade");
        ReflectionTestUtils.setField(endpointService, "clientSecret", "NfPFBF9Cjqfpd7GcOH6uVVQ9EevZHfBf");
        ReflectionTestUtils.setField(endpointService, "keycloakToken", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/token");
        ReflectionTestUtils.setField(endpointService, "keycloakLogout", "http://localhost:8180/auth/realms/idp-provider/protocol/openid-connect/logout");
        mockMvc = MockMvcBuilders.standaloneSetup(endpointController)
                .build();
    }

    @Test
    @DisplayName("Given valid params receive the token")
    void getToken() throws Exception {
        TokenData validInfo = new TokenData("mate", "mate");
        ResponseEntity<String> response = ResponseEntity.status(200).body("validToken");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(validInfo)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(response.getBody(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Given invalid params response is 401 UNAUTHORIZED")
    void tryToGetTokenWithInvalidParams() throws Exception {
        TokenData invalidInfo = new TokenData("invalidParams", "invalidParams");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(invalidInfo)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given empty strings as params response is 401 UNAUTHORIZED")
    void tryToGetTokenWithEmptyStringAsParams() throws Exception {
        TokenData invalidInfo = new TokenData("", "");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(invalidInfo)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given null params response is 401 UNAUTHORIZED")
    void tryToGetTokenWithNullParams() throws Exception {
        TokenData invalidInfo = new TokenData(null, null);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/token")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(invalidInfo)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given valid refresh token user logouts")
    void logout() throws Exception {
        TokenData validToken = new TokenData("validRefreshToken");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(validToken)))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given an invalid refresh token response is 400 BAD REQUEST")
    void tryToLogoutWithInvalidRefreshToken() throws Exception {
        TokenData invalidToken = new TokenData("invalidRefreshToken");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(invalidToken)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given an empty string as refresh token response is 400 BAD REQUEST")
    void tryToLogoutWithAnEmptyStringAsRefreshToken() throws Exception {
        TokenData emptyToken = new TokenData("");
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(emptyToken)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }

    @Test
    @DisplayName("Given a null refresh token response is 400 BAD REQUEST")
    void tryToLogoutWithNullRefreshToken() throws Exception {
        TokenData nullToken = new TokenData(null);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("keycloakMessage");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(nullToken)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(response.getStatusCodeValue(), mvcResult.getResponse().getStatus());
    }
}