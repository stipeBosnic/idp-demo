package com.example.idpfacade.controller;

import com.example.idpfacade.model.TokenData;
import com.example.idpfacade.service.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
@RestController
public class EndpointController {

    @Autowired
    EndpointService endpointService;

    @PostMapping("/logout")
    public void logout(@RequestBody TokenData tokenData) {
        endpointService.logout(tokenData);
    }

    @PostMapping("/userinfo")
    public String getUserInfo(@RequestBody TokenData tokenData) {
        return endpointService.getUserInfo(tokenData);
    }

    @PostMapping("/introspect")
    public String getIntrospect(@RequestBody TokenData tokenData) {
        return endpointService.getIntrospect(tokenData);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody TokenData data) {
        return endpointService.getToken(data);
    }
}

