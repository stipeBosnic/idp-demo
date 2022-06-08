package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.model.TokenData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProtectedDataService {

    @Autowired
    private final ProtectedDataRepository protectedDataRepository;

    @Autowired
    private final  RestTemplate restTemplate;

    @Value("${facade.protected-url}")
    private String facadeProtectedUrl;


    public List<ProtectedData> getProtectedData(String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return List.of();
        }
        return protectedDataRepository.findAll();
    }

    public ProtectedData getProtectedDataForOnePerson(String token, String insuranceNumber) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        } catch (RestClientResponseException e) {
            return null;
        }
        return protectedDataRepository.findById(insuranceNumber).orElse(null);
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
