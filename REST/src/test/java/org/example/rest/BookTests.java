package org.example.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookTests extends BaseTest {
    /* &&&&&&&&&&&&&&&
            CRUD
     &&&&&&&&&&&&&&&*/
    @Test
    public void testCreateBook() {
        String bookJson = "{ \"title\": \"Book 3\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("title", equalTo("Book 3"))
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("title", equalTo("Book 3"));
    }

    @Test
    public void testUpdateBook() {
        String bookJson = "{ \"title\": \"Book 4\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");


        String updatedBookJson = "{ \"title\": \"Book 5\" }";

        given()
            .contentType(ContentType.JSON)
            .body(updatedBookJson)
        .when()
            .put("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("title", equalTo("Book 5"))
            .body("id", equalTo(id));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("title", equalTo("Book 5"))
            .body("id", equalTo(id));
    }

    @Test
    public void testDeleteBook() {
        String bookJson = "{ \"title\": \"Book 4\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .delete("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.OK.value());

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testReadAllBooks() {
        String bookJson = "{ \"title\": \"Book 1\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");

        given()
        .when()
            .get("/api/books")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].title", equalTo("Book 1"))
            .body("[0].id", equalTo(id));
    }

    /* &&&&&&&&&&&&&&&
         negatywny
     &&&&&&&&&&&&&&&*/

    @Test
    public void testBadRequestCreate() {
        String bookJson = "{ \"title\": \"\" }";

        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testBadRequestUpdate() {
        String bookJson = "{ \"title\": \"Book 4\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(bookJson)
        .when()
            .post("/api/books")
        .then()
        .extract()
            .path("id");

        String updatedBookJson = "{ \"title\": \"\" }";

        given()
            .contentType(ContentType.JSON)
            .body(updatedBookJson)
        .when()
            .put("/api/books/" + id)
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
