package org.example.rest;

import com.mongodb.client.MongoDatabase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
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
            .statusCode(201)
            .body("title", equalTo("Book 3"))
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(200)
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
            .statusCode(200)
            .body("title", equalTo("Book 5"))
            .body("id", equalTo(id));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(200)
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
            .statusCode(200);

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/books/" + id)
        .then()
            .statusCode(404);
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
            .statusCode(400);
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
            .statusCode(400);
    }
}
