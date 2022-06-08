package com.example.idpfacade.controller;

import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class EndpointController {

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

