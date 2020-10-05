package com.oeitho.resource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.oeitho.testcontainer.InfinispanResource;

@QuarkusTest
@QuarkusTestResource(InfinispanResource.class)
public class GameResourceTest {

    @Test
    public void fetchGameNotExisting() {
        when()
            .get("api/v1/game/1")
        .then()
            .statusCode(404);
    }

    @Test
    public void createGameEndpointTest() {
        given()
            .contentType("application/json")
            .body("{ \"testInt\": 1, \"testString\": \"test\" }")
        .when()
            .post("api/v1/game")
        .then()
            .statusCode(200)
            .header("gameId", notNullValue());
    }

    @Test
    public void fetchEmptyGameAfterCreation() {
        final String gameId = createGameAndGetId("{}");
        when()
            .get("api/v1/game/" + gameId)
        .then()
            .statusCode(200)
            .body(equalTo("{}"));
    }

    @Test
    public void fetchGameWithPropertiesAfterCreation() {
        final String gameId = createGameAndGetId("{ \"testInt\": 1, \"testString\": \"test\" }");
        when()
            .get("api/v1/game/" + gameId)
        .then()
            .statusCode(200)
            .body("testInt", equalTo(1),
                  "testString", equalTo("test"));
    }

    private String createGameAndGetId(String body) {
        final Response response = 
        given()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/api/v1/game")
        .then()
            .extract()
            .response();
        return response.getHeaders().get("gameId").getValue();
    }

}