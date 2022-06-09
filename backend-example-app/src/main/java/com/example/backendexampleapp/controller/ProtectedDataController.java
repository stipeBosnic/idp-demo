package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProtectedDataController {

    private final ProtectedDataService protectedDataService;

    @GetMapping("/protected")
    public ResponseEntity<List<ProtectedData>> getProtectedData(@RequestParam String token) {
        return protectedDataService.getProtectedData(token);
    }
    @GetMapping("/protectedperson")
    public ResponseEntity<ProtectedData>  getProtectedDataForOnePerson(@RequestParam String token, @RequestParam String insuranceNumber) {
        return protectedDataService.getProtectedDataForOnePerson(token, insuranceNumber);
    }
}
