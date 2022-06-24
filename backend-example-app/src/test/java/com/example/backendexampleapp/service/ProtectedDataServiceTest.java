package com.example.backendexampleapp.service;
import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ProtectedDataServiceTest {


    @SpyBean
    ProtectedDataService protectedDataService;

    @MockBean
    ProtectedDataRepository protectedDataRepository;

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

        List<ProtectedData> expectedProtectedData= Stream.of(person1,person2).toList();
        when(protectedDataRepository.findAll()).thenReturn(expectedProtectedData);
        List<ProtectedData> response = protectedDataService.getProtectedData();
        assertEquals(expectedProtectedData, response);
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

        when(protectedDataRepository.findById(anyString())).thenReturn(Optional.of(protectedData));
        assertEquals(protectedData, protectedDataService.getProtectedDataForOnePerson("validInsuranceNumber"));
    }
}
