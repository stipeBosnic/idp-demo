package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.model.TokenData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProtectedDataService {

    private final ProtectedDataRepository protectedDataRepository;

    private final  RestTemplate restTemplate;

    @Value("${facade.protected-url}")
    String facadeProtectedUrl;


    public ResponseEntity<List<ProtectedData>> getProtectedData(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(protectedDataRepository.findAll());
    }

    public ResponseEntity<ProtectedData> getProtectedDataForOnePerson(String token, String insuranceNumber) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(protectedDataRepository.findById(insuranceNumber).orElse(null));
    }

    @PostConstruct
    public void initializeProtectedDataTable() {
        protectedDataRepository.saveAll(
                Stream.of(
                        new ProtectedData("Max", "Musterman", 25, "m", "123456"),
                        new ProtectedData("Judit", "Backenbauer", 36, "f", "654321")
                ).collect(Collectors.toList()));
    }
}
