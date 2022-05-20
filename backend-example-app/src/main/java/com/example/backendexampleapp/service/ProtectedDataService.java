package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.repository.ProtectedDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProtectedDataService {

    @Autowired
    ProtectedDataRepository protectedDataRepository;

    public List<ProtectedData> getProtectedData() {
        return protectedDataRepository.findAll();
    }

    @PostConstruct
    public void initializeProtectedDataTable() {
        protectedDataRepository.saveAll(
                Stream.of(
                        new ProtectedData("Max", "Musterman", 25 , "m", "123456"),
                        new ProtectedData("Judit", "Backenbauer", 36 , "f", "654321")
                ).collect(Collectors.toList()));
    }
}
