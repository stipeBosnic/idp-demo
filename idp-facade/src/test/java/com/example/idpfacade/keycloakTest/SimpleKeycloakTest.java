//package com.example.idpfacade.keycloakTest;
//
//import dasniko.testcontainers.keycloak.KeycloakContainer;
//import io.restassured.response.ValidatableResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import java.util.Map;
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@Testcontainers
//class SimpleKeycloakTest {
//    @Container
//    KeycloakContainer keycloakContainer = new KeycloakContainer("jboss/keycloak:16.1.1")
//            .withRealmImportFile("/idp-provider-realm.json");
//
//    @Test
//    @DisplayName("Test if keycloak sends the token when given valid data")
//    void testKeycloakTest() {
//
//        assertTrue(keycloakContainer.isRunning());
//
//        String authServerUrl = keycloakContainer.getAuthServerUrl();
//        System.out.println(authServerUrl);
//
//        String accessToken = given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParams(Map.of(
//                        "username", "mate",
//                        "password", "mate",
//                        "grant_type", "password",
//                        "client_id", "idp-facade",
//                        "client_secret", "secret"
//                ))
//                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
//                .then().assertThat().statusCode(200)
//                .extract().path("access_token");
//
//        System.out.println("My access token: "+accessToken);
//    }
//    @Test
//    @DisplayName("Test if unauthorized user can access the token")
//    void testKeycloakUnauthorizedUserTest() {
//
//        String authServerUrl = keycloakContainer.getAuthServerUrl();
//
//        ValidatableResponse response = given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParams(Map.of(
//                        "username", "unknownUser",
//                        "password", "unknownPassword",
//                        "grant_type", "password",
//                        "client_id", "idp-facade",
//                        "client_secret", "secret"
//                ))
//                .post(authServerUrl+"/realms/idp-provider/protocol/openid-connect/token")
//                .then().assertThat().statusCode(401);
//    }
//}
