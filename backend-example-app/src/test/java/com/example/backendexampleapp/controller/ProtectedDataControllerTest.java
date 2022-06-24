package com.example.backendexampleapp.controller;
import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(ProtectedDataController.class)
class ProtectedDataControllerTest {
    @SpyBean
    private ProtectedDataController protectedDataController;

    @MockBean
    private ProtectedDataService protectedDataService;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    @DisplayName("When given valid access token protected data is returned")
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

        List<ProtectedData> protectedData = Stream.of(person1, person2).toList();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(ResponseEntity.status(200).body("token"));
        when(protectedDataService.getProtectedData()).thenReturn(protectedData);
        ResponseEntity<List<ProtectedData>> response = protectedDataController.getProtectedData("validToken");
        assertEquals(protectedData, response.getBody());
    }

    @Test
    @DisplayName("When given an invalid access token response is 401 unauthorized")
    void tryToGetProtectedDataWithInvalidTokenTest() {

        HttpClientErrorException expectedException = new HttpClientErrorException("", HttpStatus.UNAUTHORIZED, "", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(expectedException);
        assertEquals(expectedException.getStatusCode(), protectedDataController.getProtectedData("invalidToken").getStatusCode());
    }

    @Test
    @DisplayName("When given an empty string as token response is 401 unauthorized")
    void tryToGetProtectedDataWithEmptyStringAsTokenTest() {

        HttpClientErrorException expectedException = new HttpClientErrorException("", HttpStatus.UNAUTHORIZED, "", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(expectedException);
        assertEquals(expectedException.getStatusCode(), protectedDataController.getProtectedData("").getStatusCode());
    }

    @Test
    @DisplayName("When given a valid access token and insurance number protected data is returned")
    void getProtectedDataForOnePersonTest() {

        ProtectedData expectedResponse = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();

        Mockito.when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(ResponseEntity.status(200).body("token"));
        Mockito.when(protectedDataService.getProtectedDataForOnePerson("validInsuranceNumber")).thenReturn(expectedResponse);
        assertEquals(expectedResponse, protectedDataController.getProtectedDataForOnePerson("validToken", "validInsuranceNumber").getBody());
    }

    @Test
    @DisplayName("When given an invalid access token and insurance number response is 401")
    void tryToGetProtectedDataForOnePersonWithInvalidTokenTest() {
        HttpClientErrorException expectedException = new HttpClientErrorException("", HttpStatus.UNAUTHORIZED, "", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(expectedException);
        assertEquals(expectedException.getStatusCode(), protectedDataController.getProtectedDataForOnePerson("invalidToken", "invalidNumber").getStatusCode());
    }

    @Test
    @DisplayName("When given an empty string as access token and insurance number response is 401")
    void tryToGetProtectedDataForOnePersonWithEmptyStringAsTokenTest() {
        HttpClientErrorException expectedException = new HttpClientErrorException("", HttpStatus.UNAUTHORIZED, "", null, null, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(expectedException);
        assertEquals(expectedException.getStatusCode(), protectedDataController.getProtectedDataForOnePerson("","").getStatusCode());
    }
}