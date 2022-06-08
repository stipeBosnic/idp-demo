package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ProtectedDataServiceTest {

    @MockBean
    private ProtectedDataRepository protectedDataRepository;

    @SpyBean
    private ProtectedDataService protectedDataService;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(protectedDataService, "facadeProtectedUrl", "http://localhost:8090/userinfo");
    }

    @Test
    @DisplayName("Given a valid access token receive protected data")
    void getProtectedData() {

        ProtectedData person1 = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();

        ProtectedData person2 = ProtectedData
                .builder()
                .name("Judith")
                .surname("Backenbauer")
                .old(36)
                .gender("f")
                .insuranceNumber("654321")
                .build();

        List<ProtectedData> expectedProtectedData = List.of(person1, person2).stream().toList();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("validToken");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedResponse);
        when(protectedDataRepository.findAll()).thenReturn(expectedProtectedData);
        List<ProtectedData> response = protectedDataService.getProtectedData("validToken");
        assertEquals(expectedProtectedData, response);
    }

    @Test
    @DisplayName("Given a valid access token and users insurance number receive data about that user")
    void getProtectedDataForPerson1() {

        ProtectedData protectedData = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();

        ResponseEntity<String> expectedToken = ResponseEntity.ok("validToken");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(expectedToken);
        when(protectedDataRepository.findById(anyString())).thenReturn(Optional.of(protectedData));
        ProtectedData response = protectedDataService.getProtectedDataForOnePerson(expectedToken.getBody(), "123456");
        assertEquals(protectedData, response);
    }

    @Test
    @DisplayName("Given an invalid access token return empty list")
    void getProtectedDataWithInvalidToken() {
        assertEquals(List.of(), protectedDataService.getProtectedData("invalidToken"));
    }
    @Test
    @DisplayName("Given a null access token return empty list")
    void getProtectedDataWithNullToken() {
        assertEquals(List.of(), protectedDataService.getProtectedData(null));
    }
    @Test
    @DisplayName("Given an empty string as access token return empty list")
    void getProtectedDataWithEmptyToken() {
        assertEquals(List.of(), protectedDataService.getProtectedData(""));
    }

}
