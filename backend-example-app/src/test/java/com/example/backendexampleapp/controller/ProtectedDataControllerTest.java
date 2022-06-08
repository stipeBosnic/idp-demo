package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.example.backendexampleapp.service.FacadeEndpointService;
import com.example.backendexampleapp.service.ProtectedDataService;
import com.google.gson.Gson;
import org.apache.http.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProtectedDataController.class)
class ProtectedDataControllerTest {
    @SpyBean
    private ProtectedDataController protectedDataController;
    @MockBean
    private ProtectedDataService protectedDataService;


    @Test
    @DisplayName("When given valid access token protected data is returned")
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

        List<ProtectedData> protectedData = List.of(person1, person2).stream().toList();
        Mockito.when(protectedDataService.getProtectedData("validToken")).thenReturn(protectedData);
        List<ProtectedData> response = protectedDataController.getProtectedData("validToken");
        assertEquals(protectedData, response);
    }

    @Test
    @DisplayName("When given a valid access token and insurance number protected data is returned")
    void getProtectedDataForOnePerson() {

        ProtectedData person = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();


        Mockito.when(protectedDataService.getProtectedDataForOnePerson("validToken", "insuranceNumber")).thenReturn(person);
        ProtectedData response = protectedDataController.getProtectedDataForOnePerson("validToken", "insuranceNumber");
        assertEquals(person, response);
    }

    @Test
    @DisplayName("When given a null access token empty list is returned")
    void getProtectedDataWithNullToken() {

        Mockito.when(protectedDataService.getProtectedData(null)).thenReturn(List.of());
        List<ProtectedData> response = protectedDataController.getProtectedData(null);
        assertEquals(List.of(), response);
    }

    @Test
    @DisplayName("When given an empty string as access token empty list is returned")
    void getProtectedDataWithEmptyToken() {

        Mockito.when(protectedDataService.getProtectedData("")).thenReturn(List.of());
        List<ProtectedData> response = protectedDataController.getProtectedData("");
        assertEquals(List.of(), response);

    }

    @Test
    @DisplayName("When given an invalid access token empty list is returned")
    void getProtectedDataWithInvalidToken() {

        Mockito.when(protectedDataService.getProtectedData("invalidToken")).thenReturn(List.of());
        List<ProtectedData> response = protectedDataController.getProtectedData("invalidToken");
        assertEquals(List.of(), response);

    }

    @Test
    @DisplayName("When given an invalid access token and insurance number null is returned")
    void getProtectedDataForOnePersonWithInvalidToken() {

        Mockito.when(protectedDataService.getProtectedDataForOnePerson("invalidToken", "invalidInsuranceNumber")).thenReturn(null);
        ProtectedData response = protectedDataController.getProtectedDataForOnePerson("invalidToken", "invalidInsuranceNumber");
        assertEquals(null, response);
    }
}