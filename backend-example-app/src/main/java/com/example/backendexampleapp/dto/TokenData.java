package com.example.backendexampleapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenData {

    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String token;

    private String refreshToken;


    public TokenData(String token) {
        this.token = token;
    }

    public TokenData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public TokenData(String clientId, String clientSecret, String token) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.token = token;
    }

    public TokenData(String username, String password, String clientId, String clientSecret) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public static TokenData createRefresherToken(String refreshToken) {
        TokenData data = new TokenData();
        data.setRefreshToken(refreshToken);
        return data;
    }

}
