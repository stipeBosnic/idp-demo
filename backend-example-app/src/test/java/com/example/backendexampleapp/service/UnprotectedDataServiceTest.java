package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.UnprotectedData;
import com.example.backendexampleapp.repository.UnprotectedDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UnprotectedDataServiceTest {


    UnprotectedDataService unprotectedDataService;
    @Mock
    UnprotectedDataRepository unprotectedDataRepository;

    @BeforeEach
    void setUp() {
        unprotectedDataService = new UnprotectedDataService(unprotectedDataRepository);
    }

    @Test
    @DisplayName("Anyone receives unprotected data")
    void getUnprotectedData() {
        UnprotectedData unprotectedData = new UnprotectedData("Data", Date.from(Instant.now()));
        List unprotectedDataList = new ArrayList();
        unprotectedDataList.add(unprotectedData);
        when(unprotectedDataRepository.findAll()).thenReturn(unprotectedDataList);
        List<UnprotectedData> expectedList = unprotectedDataService.getUnprotectedData();
        assertEquals(unprotectedDataList, expectedList);
    }
}