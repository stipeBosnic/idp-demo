package com.example.backendexampleapp.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ProtectedDataServiceTest {


    private final ProtectedDataService dataService = new ProtectedDataService();


    private final RestTemplate restTemplate = new RestTemplate();

//    @BeforeEach
//    void setup() {
//        dataService = new ProtectedDataService();
//    }

//    @Test
//    @DisplayName("Given insurance Number = 123456 WHEN getProtectedData from the service THEN return data for this person")
//    void getProtectedDataForInsuredPerson() {
//        ProtectedData data = ProtectedData.builder()
//                .name("Max")
//                .surname("Musterman")
//                .old(25)
//                .gender("m")
//                .insuranceNumber("123456")
//                .build();
//        assertEquals(data, dataService.getProtectedData("123456"));
//    }
//    @Test
//    @DisplayName("Given insurance Number = 654321 WHEN getProtectedData from the service THEN return data for this person")
//    void getProtectedDataForOtherInsuredPerson() {
//        ProtectedData data = ProtectedData.builder()
//                .name("Judith")
//                .surname("Backenbauer")
//                .old(36)
//                .gender("f")
//                .insuranceNumber("654321")
//                .build();
//        assertEquals(data, dataService.getProtectedData("654321"));
//    }
//
//    @Test
//    @DisplayName("Given unknown insurance Number data WHEN getProtectedData from DataService THEN return null")
//    void getProtectedDataForUnknownPerson() {
//        assertNull(dataService.getProtectedData("unknown insurance number"));
//    }
//    @Test
//    @DisplayName("Given null data WHEN getProtectedData from DataService THEN throw IllegalArgumentException")
//    void getProtectedDataForUnknownArgument() {
//        assertThrows(IllegalArgumentException.class,() -> {
//            dataService.getProtectedData(null);} );
//    }
}