package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.model.ProtectedData;
import com.example.backendexampleapp.service.ProtectedDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProtectedDataController {
    ProtectedDataService protectedDataService;

    @GetMapping("/protected")
    public List<ProtectedData> getProtectedData(@RequestParam String token ) {
        return protectedDataService.getProtectedData(token);
    }
    @GetMapping("/protectedperson")
    public ProtectedData getProtectedDataForOnePerson(@RequestParam String token, @RequestParam String insuranceNumber) {
        return protectedDataService.getProtectedDataForOnePerson(token, insuranceNumber);
    }
}
