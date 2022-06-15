package com.example.backendexampleapp.controller;
import com.example.backendexampleapp.dto.TokenData;
import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProtectedDataController {

    private final ProtectedDataService protectedDataService;

    private final RestTemplate restTemplate;

    @Value("${facade.protected-url}")
    String facadeProtectedUrl;

    @GetMapping("/protected")
    public ResponseEntity<List<ProtectedData>> getProtectedData(@RequestParam String token) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        }  catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(null);
        }
        return protectedDataService.getProtectedData();
    }
    @GetMapping("/protectedperson")
    public ResponseEntity<Optional<ProtectedData>> getProtectedDataForOnePerson(@RequestParam String token, @RequestParam String insuranceNumber) {
        TokenData data = new TokenData(token);
        HttpEntity<TokenData> request = new HttpEntity<>(data, null);
        try {
            restTemplate.exchange(facadeProtectedUrl, HttpMethod.POST, request, String.class);
        }  catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getRawStatusCode()).body(null);
        }
        return protectedDataService.getProtectedDataForOnePerson(insuranceNumber);
    }
}
