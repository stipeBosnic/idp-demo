package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.model.TokenData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProtectedDataService {

    @Autowired
    ProtectedDataRepository protectedDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ProtectedData> getProtectedData(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/userinfo", HttpMethod.POST, request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return protectedDataRepository.findAll();
        } else {
            return null;
        }
    }

    public ProtectedData getProtectedDataForOnePerson(String token, String insuranceNumber) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8090/userinfo", HttpMethod.POST, request, String.class);
        if (response.getStatusCodeValue() == 200) {
            return protectedDataRepository.findById(insuranceNumber).orElse(null);
        } else {
            return null;
        }
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
