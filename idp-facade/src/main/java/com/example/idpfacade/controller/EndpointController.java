package com.example.idpfacade.controller;

import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EndpointController {

    @Autowired
    EndpointService endpointService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenData tokenData) {
        return endpointService.logout(tokenData);
    }

    @PostMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestBody TokenData tokenData) {
        return endpointService.getUserinfo(tokenData);
    }

    @PostMapping("/introspect")
    public ResponseEntity<String> getIntrospect(@RequestBody TokenData tokenData) {
        return endpointService.getIntrospect(tokenData);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody TokenData data) {
        return endpointService.getToken(data);
    }
}

