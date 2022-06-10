package com.example.backendexampleapp.service;

import com.example.backendexampleapp.model.UnprotectedData;
import com.example.backendexampleapp.repository.UnprotectedDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UnprotectedDataService {

    private final UnprotectedDataRepository unprotectedDataRepository;

    private final Date date = Date.from(Instant.now());

    public List<UnprotectedData> getUnprotectedData() {
        return unprotectedDataRepository.findAll();
    }

    @PostConstruct
    public void initializeUnprotectedDataTable() {
        unprotectedDataRepository.saveAll(
                Stream.of(
                        new UnprotectedData("Lorem Ipsum is simply dummy text of the printing and typesetting industry",date)
                ).collect(Collectors.toList()));
    }
}
