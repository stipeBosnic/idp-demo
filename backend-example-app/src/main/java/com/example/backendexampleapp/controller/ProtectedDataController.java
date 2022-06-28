package com.example.backendexampleapp.controller;
import com.example.backendexampleapp.dto.TokenData;
import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProtectedDataController {

    private final ProtectedDataService protectedDataService;

    private final RestTemplate restTemplate;

    @Value("${facade.protected-url}")
    String facadeProtectedUrl;


    @GetMapping("/protected")
    public ResponseEntity<List<ProtectedData>> getProtectedData() {
        return ResponseEntity.status(HttpStatus.OK).body(protectedDataService.getProtectedData());
    }

    @GetMapping("/protectedperson")
    public ResponseEntity<?> getProtectedDataForOnePerson(@RequestParam String token, @RequestParam String insuranceNumber) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(protectedDataService.getProtectedDataForOnePerson(insuranceNumber));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }
}
