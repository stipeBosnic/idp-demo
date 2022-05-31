package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.UnprotectedData;
import com.example.backendexampleapp.service.UnprotectedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class UnprotectedDataController {

    @Autowired
    UnprotectedDataService unprotectedDataService;

    @GetMapping("/unprotected")
    public List<UnprotectedData> getUnprotectedData() {
        return unprotectedDataService.getUnprotectedData();
    }
}
