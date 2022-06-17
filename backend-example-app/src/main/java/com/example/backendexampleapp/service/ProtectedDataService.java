package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProtectedDataService {

    private final ProtectedDataRepository protectedDataRepository;

    @Value("${facade.protected-url}")
    String facadeProtectedUrl;


    public ResponseEntity<List<ProtectedData>> getProtectedData() {
        return ResponseEntity.status(HttpStatus.OK).body(protectedDataRepository.findAll());
    }

    public ResponseEntity<ProtectedData> getProtectedDataForOnePerson(String insuranceNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(protectedDataRepository.findById(insuranceNumber)
                .orElseThrow(() -> new IllegalStateException("Person " + insuranceNumber + " does not exist!")));
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
