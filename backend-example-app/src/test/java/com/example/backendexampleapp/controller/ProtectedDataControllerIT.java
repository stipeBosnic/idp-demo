package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.example.backendexampleapp.service.FacadeEndpointService;
import com.example.backendexampleapp.service.ProtectedDataService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProtectedDataController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ProtectedDataControllerIT {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ProtectedDataService protectedDataService;
    @SpyBean
    ProtectedDataController protectedDataController;

    @MockBean
    ProtectedDataRepository protectedDataRepository;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(protectedDataService, "facadeProtectedUrl", "http://localhost:8090/userinfo");
        mockMvc = MockMvcBuilders.standaloneSetup(protectedDataController)
                .build();
    }

    @Test
    @DisplayName("When given valid access token protected data is returned")
    void getProtectedDataTest() throws Exception {


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
        String protectedDataJson = new Gson().toJson(protectedData);
        ResponseEntity<String> answer = ResponseEntity.status(200).body("userinfo");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(answer);
        when(protectedDataRepository.findAll()).thenReturn(protectedData);
        MvcResult mvcResult = mockMvc.perform(get("/protected")
                        .param("token", "validToken"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(protectedDataJson, mvcResult.getResponse().getContentAsString());
//        verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class)))
    }

    @Test
    @DisplayName("When given invalid access token empty list is returned")
    void tryToGetProtectedDataWithInvalidTokenTest() throws Exception {

        ResponseEntity<String> response = ResponseEntity.status(401).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        when(protectedDataRepository.findAll()).thenReturn(List.of());
        MvcResult mvcResult = mockMvc.perform(get("/protected")
                        .param("token", "invalidToken"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(List.of().toString(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("When given empty string as an access token empty list is returned")
    void tryToGetProtectedDataWithAnEmptyStringAsTokenTest() throws Exception {

        ResponseEntity<String> response = ResponseEntity.status(401).body("");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(response);
        when(protectedDataRepository.findAll()).thenReturn(List.of());
        MvcResult mvcResult = mockMvc.perform(get("/protected")
                        .param("token", ""))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(List.of().toString(), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("When given valid access token and insurance number protected data is returned")
    void getProtectedDataForOnePersonTest() throws Exception {

        ProtectedData person = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();
        String personJson = new Gson().toJson(person);
        ResponseEntity<String> answer = ResponseEntity.status(200).body("userinfo");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(answer);
        when(protectedDataRepository.findById(person.getInsuranceNumber())).thenReturn(Optional.of(person));

        MvcResult mvcResult = mockMvc.perform(get("/protectedperson")
                        .param("token", "validToken")
                        .param("insuranceNumber", person.getInsuranceNumber()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(personJson, mvcResult.getResponse().getContentAsString());

    }
}
