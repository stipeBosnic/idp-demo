package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProtectedDataServiceTest {

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
        ProtectedDataService protectedDataService = new ProtectedDataService();

        String token =  facadeEndpointService.getToken("mate", "mate").getBody();
        Map jsonToken = new Gson().fromJson(token, Map.class);
        String accessToken = jsonToken.get("access_token").toString();
        ProtectedData data = protectedDataService.getProtectedDataForOnePerson(accessToken, "123456");
        assertEquals(protectedData, data);
    }
}