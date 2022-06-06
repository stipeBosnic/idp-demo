package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.example.backendexampleapp.service.FacadeEndpointService;
import com.example.backendexampleapp.service.ProtectedDataService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProtectedDataController.class)
public class ProtectedDataControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProtectedDataService protectedDataService;

    @Mock
    private ProtectedDataRepository protectedDataRepository;

    @Mock
    private FacadeEndpointService facadeEndpointService;

    @Mock
    private ProtectedDataController protectedDataController;

    @MockBean
    RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        protectedDataService = new ProtectedDataService(protectedDataRepository, restTemplate);
        facadeEndpointService = new FacadeEndpointService(restTemplate);
        protectedDataController = new ProtectedDataController(protectedDataService);
        ReflectionTestUtils.setField(facadeEndpointService, "facadeTokenUrl", "http://localhost:8090/token");
        ReflectionTestUtils.setField(protectedDataService, "facadeProtectedUrl", "http://localhost:8090/userinfo");
    }

    @Test
    @DisplayName("When given valid access token protected data is returned")
    void getProtectedData() throws Exception {

        String token = facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String accessToken = jsonToken.get("access_token").toString();

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
        Mockito.when(protectedDataService.getProtectedData(accessToken)).thenReturn(protectedData);
        mockMvc.perform(get("/protected")
                .param("token", accessToken))
                .andExpect(status().isOk())
                .andExpect(result -> equals(protectedData));
    }
    @Test
    @DisplayName("When given valid access token and insurance number protected data is returned")
    void getProtectedDataForOnePerson() throws Exception {

        String token = facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String accessToken = jsonToken.get("access_token").toString();

        ProtectedData person = ProtectedData
                .builder()
                .name("Max")
                .surname("Musterman")
                .old(25)
                .gender("m")
                .insuranceNumber("123456")
                .build();


//        Mockito.when(protectedDataService.getProtectedDataForOnePerson(accessToken, person.getInsuranceNumber())).thenReturn(Optional.ofNullable(person));
        mockMvc.perform(get("/protectedperson")
                        .param("token", accessToken)
                        .param("insuranceNumber", person.getInsuranceNumber()))
                        .andExpect(status().isOk())
                        .andExpect(result -> equals(person));
    }

}
