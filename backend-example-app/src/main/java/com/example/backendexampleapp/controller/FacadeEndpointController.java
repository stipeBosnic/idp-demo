package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.service.FacadeEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class FacadeEndpointController {

    @Autowired
    FacadeEndpointService facadeEndpointService;

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken) {
        return facadeEndpointService.logout(refreshToken);
    }
    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam String username, @RequestParam String password) {
       return facadeEndpointService.getToken(username, password);
    }
    @PostMapping("/introspect")
    public ResponseEntity<String> getIntrospect(@RequestParam String token) {
        return facadeEndpointService.getIntrospect(token);
    }
    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestParam String token) {
        return facadeEndpointService.getUserInfo(token);
    }
}
