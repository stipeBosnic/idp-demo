package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ProtectedDataServiceTest {

    @MockBean
    private ProtectedDataRepository protectedDataRepository;
    @InjectMocks
    private ProtectedDataService protectedDataService;

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

        FacadeEndpointService facadeEndpointService = new FacadeEndpointService();
        String token = facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String accessToken = jsonToken.get("access_token").toString();
        ProtectedData data = protectedDataService.getProtectedDataForOnePerson(accessToken, "123456");
        assertEquals(protectedData, data);
    }

    @Test
    void getProtectedDataForOnePerson() {
    }
}