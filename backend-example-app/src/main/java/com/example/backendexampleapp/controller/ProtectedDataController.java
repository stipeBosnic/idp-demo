package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProtectedDataController {

    @Autowired
    ProtectedDataService protectedDataService;

    @GetMapping("/protected")
    public List<ProtectedData> getProtectedData() {
        return protectedDataService.getProtectedData();
    }
}