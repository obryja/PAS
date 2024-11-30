package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class RentTests extends BaseTest {
    /* &&&&&&&&&&&&&&&
           rent
     &&&&&&&&&&&&&&&*/

    @Test
    public void testRent() {
        String bookJson = "{ \"title\": \"Book 1\" }";

        String bookId =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");

        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String userId =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");


        String rentJson = "{ \"userId\": \"" + userId + "\", \"bookId\": \"" + bookId + "\", \"beginDate\": \"2024-11-21T14:31:16.165\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(rentJson)
        .when()
            .post("/api/rents")
        .then()
            .statusCode(201)
            .body("userId", equalTo(userId))
            .body("bookId", equalTo(bookId))
            .body("beginDate", equalTo("2024-11-21T14:31:16.165"))
            .body("endDate", equalTo(null))
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/rents/" + id)
        .then()
            .statusCode(200)
            .body("userId", equalTo(userId))
            .body("bookId", equalTo(bookId))
            .body("beginDate", equalTo("2024-11-21T14:31:16.165"))
            .body("endDate", equalTo(null));
    }

    /* &&&&&&&&&&&&&&&
         negatywny
     &&&&&&&&&&&&&&&*/

    @Test
    public void testConflictRent() {
        String bookJson = "{ \"title\": \"Book 1\" }";

        String bookId =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");

        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String userId =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");


        String rentJson = "{ \"userId\": \"" + userId + "\", \"bookId\": \"" + bookId + "\", \"beginDate\": \"2024-11-21T14:31:16.165\" }";

        given()
            .contentType(ContentType.JSON)
            .body(rentJson)
        .when()
            .post("/api/rents");

        given()
            .contentType(ContentType.JSON)
            .body(rentJson)
        .when()
            .post("/api/rents")
        .then()
            .statusCode(409);
    }
}
