package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientResponseException;
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


    @SpyBean
    ProtectedDataService protectedDataService;

    @MockBean
    ProtectedDataRepository protectedDataRepository;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(protectedDataService, "facadeProtectedUrl", "http://localhost:8090/userinfo");
    }

    @Test
    @DisplayName("Given a valid access token receive protected data")
    void getProtectedDataTest() {

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
        ResponseEntity<List<ProtectedData>> response = protectedDataService.getProtectedData("validToken");
        assertEquals(expectedProtectedData, response.getBody());
    }

    @Test
    @DisplayName("Given a valid access token and users insurance number receive data about that user")
    void getProtectedDataForASinglePersonTest() {

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
        ResponseEntity<ProtectedData> response = protectedDataService.getProtectedDataForOnePerson(expectedToken.getBody(), "validNumber");
        assertEquals(protectedData, response.getBody());
    }

    @Test
    @DisplayName("Given an invalid access token return 401 UNAUTHORIZED")
    void getProtectedDataWithInvalidTokenTest() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(RestClientResponseException.class);
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null), protectedDataService.getProtectedData("invalidToken"));
    }

    @Test
    @DisplayName("Given a null access token return 401 UNAUTHORIZED")
    void getProtectedDataWithNullTokenTest() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(RestClientResponseException.class);
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null), protectedDataService.getProtectedData(null));
    }

    @Test
    @DisplayName("Given an empty string as access token return 401 UNAUTHORIZED")
    void getProtectedDataWithEmptyTokenTest() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(RestClientResponseException.class);
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null), protectedDataService.getProtectedData(""));
    }

}
