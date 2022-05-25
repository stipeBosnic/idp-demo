package com.example.backendexampleapp.controller;

import com.example.backendexampleapp.service.FacadeEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FacadeEndpointController {

    @Autowired
    FacadeEndpointService facadeEndpointService;
    @GetMapping("/logout")
    public String logout(@RequestParam String refreshToken) {
        return facadeEndpointService.logout(refreshToken);
    }
    @PostMapping("/token")
    public String getToken(@RequestParam String username, @RequestParam String password) {
       return facadeEndpointService.getToken(username, password);
    }
    @PostMapping(path = "/introspect")
    public String  getIntrospect(@RequestParam String token) {
        return facadeEndpointService.getIntrospect(token);
    }
    @GetMapping(path = "/userinfo")
    public String getUserInfo(@RequestParam String token) {
        return facadeEndpointService.getUserInfo(token);
    }
}
