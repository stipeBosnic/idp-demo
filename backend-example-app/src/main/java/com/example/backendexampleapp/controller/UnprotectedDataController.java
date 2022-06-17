package com.example.backendexampleapp.controller;
import com.example.backendexampleapp.model.UnprotectedData;
import com.example.backendexampleapp.service.UnprotectedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UnprotectedDataController {
    private final UnprotectedDataService unprotectedDataService;

    @GetMapping("/unprotected")
    public List<UnprotectedData> getUnprotectedData() {
        return unprotectedDataService.getUnprotectedData();
    }
}
