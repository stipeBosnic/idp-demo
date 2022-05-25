package com.example.idpfacade.keycloakTest;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
public class SimpleKeycloakTest {
    @Container
    KeycloakContainer keycloakContainer = new KeycloakContainer("jboss/keycloak:16.1.1")
            .withRealmImportFile("/idp-provider-realm.json");

    @Test
    public void testKeycloak() {

        assertTrue(keycloakContainer.isRunning());

        String authServerUrl = keycloakContainer.getAuthServerUrl();
        System.out.println(authServerUrl);
        System.out.println(keycloakContainer.getAdminPassword());
        System.out.println(keycloakContainer.getAdminUsername());

        String accessToken = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "username", "mate",
                        "password", "mate",
                        "grant_type", "password",
                        "client_id", "idp-facade",
                        "client_secret", "NfPFBF9Cjqfpd7GcOH6uVVQ9EevZHfBf"
                ))
                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
                .then().assertThat().statusCode(200)
                .extract().path("access_token");

        System.out.println("My access token: "+accessToken);
    }
}
