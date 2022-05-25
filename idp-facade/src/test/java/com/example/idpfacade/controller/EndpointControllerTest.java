package com.example.idpfacade.controller;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Map;
import static io.restassured.RestAssured.given;

@Testcontainers
class EndpointControllerTest {

    @Container
    KeycloakContainer keycloakContainer = new KeycloakContainer("jboss/keycloak:16.1.1")
            .withRealmImportFile("/idp-provider-realm.json");

    @Test
    void logout() {
        String authServerUrl = keycloakContainer.getAuthServerUrl();
        String refreshToken = getRefreshToken("mate", "mate");

        ValidatableResponse response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "client_id", "idp-facade",
                        "client_secret", "secret",
                        "refresh_token", refreshToken)
                )
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/logout")
                .then().assertThat().statusCode(204);
    }

    @Test
    void getUserInfo() {
        String authServerUrl = keycloakContainer.getAuthServerUrl();
        String token = getAccessToken("mate", "mate");

        String response = given()
                .headers(Map.of(
                        "Authorization", "Bearer "+token,
                        "Content-Type", "application/x-www-form-urlencoded"
                ))
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/userinfo")
                .then().assertThat().statusCode(200)
                .extract().asString();
        System.out.println("Extracted response: "+response);
    }

    @Test
    void getIntrospect() {
        String authServerUrl = keycloakContainer.getAuthServerUrl();
        String accessToken = getAccessToken("mate", "mate");

        String response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "client_id", "idp-facade",
                        "client_secret", "secret",
                        "token", accessToken)
                )
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token/introspect")
                .then().assertThat().statusCode(200)
                .extract().asString();
        System.out.println("Extracted response: "+response);
    }

    @Test
    void getToken() {
        String authServerUrl = keycloakContainer.getAuthServerUrl();

        String response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "client_id", "idp-facade",
                        "client_secret", "secret",
                        "username", "mate",
                        "password", "mate",
                        "grant_type", "password")
                )
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
                .then().assertThat().statusCode(200)
                .extract().asString();
        System.out.println("Extracted response: "+response);
    }

    public String getAccessToken(String username, String password) {

        String authServerUrl = keycloakContainer.getAuthServerUrl();

        return given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "username", username,
                        "password", password,
                        "grant_type", "password",
                        "client_id", "idp-facade",
                        "client_secret", "secret"
                ))
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
                .then().assertThat().statusCode(200)
                .extract().path("access_token");

    }
    public String getRefreshToken(String username, String password) {

        String authServerUrl = keycloakContainer.getAuthServerUrl();
        return given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "username", username,
                        "password", password,
                        "grant_type", "password",
                        "client_id", "idp-facade",
                        "client_secret", "secret"
                ))
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
                .then().assertThat().statusCode(200)
                .extract().path("refresh_token");
    }

}