package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.UnprotectedData;
import com.example.backendexampleapp.service.UnprotectedDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@WebMvcTest(UnprotectedDataController.class)
class UnprotectedDataControllerTest {

    @SpyBean
    UnprotectedDataController unprotectedDataController;

    @MockBean
    UnprotectedDataService unprotectedDataService;

    @Test
    @DisplayName("When making a call to the unprotected data endpoint receive unprotected data")
    void getUnprotectedData() {
        List<UnprotectedData> unprotectedData = new ArrayList<>();
        when(unprotectedDataService.getUnprotectedData()).thenReturn(unprotectedData);
        List<UnprotectedData> expectedResponse = unprotectedDataController.getUnprotectedData();
        assertEquals(unprotectedData, expectedResponse);

    }
}