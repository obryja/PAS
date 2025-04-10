package org.example.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UserTests extends BaseTest {
    /* &&&&&&&&&&&&&&&
            CRU
     &&&&&&&&&&&&&&&*/
    @Test
    public void testCreateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
            .statusCode(201)
            .body("username", equalTo("testclient"))
            .body("password", equalTo("test"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"))
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("username", equalTo("testclient"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testUpdateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\"}";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");

        String updatedUserJson = "{ \"username\": \"diff\", \"password\": \"test\" }";

        given()
            .contentType(ContentType.JSON)
            .body(updatedUserJson)
        .when()
            .put("/api/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("username", equalTo("diff"))
            .body("password", equalTo("test"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("username", equalTo("diff"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testActivateAndDeactivateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/users/" + id + "/deactivate")
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("active", equalTo(false));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("active", equalTo(false));

        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/users/" + id + "/activate")
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("active", equalTo(true));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("active", equalTo(true));
    }

    @Test
    public void testGetAllUsers() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
            .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].id", equalTo(id))
            .body("[0].username", equalTo("testclient"))
            .body("[0].active", equalTo(true))
            .body("[0].role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testGetUserSByUsername() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/username/testclient")
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("username", equalTo("testclient"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/username?username=test")
        .then()
            .statusCode(200)
            .body("size()", is(1))
            .body("[0].id", equalTo(id))
            .body("[0].username", equalTo("testclient"))
            .body("[0].active", equalTo(true))
            .body("[0].role", equalTo("ROLE_CLIENT"));
    }

    /* &&&&&&&&&&&&&&&
         negatywny
     &&&&&&&&&&&&&&&*/

    @Test
    public void testBadRequestCreate() {
        String userJson = "{ \"username\": \"d\", \"password\": \"test\" }";

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
            .statusCode(400);
    }

    @Test
    public void testBadRequestUpdate() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
        .extract()
            .path("id");

        String updatedUserJson = "{ \"username\": \"d\", \"password\": \"test\" }";

        given()
            .contentType(ContentType.JSON)
            .body(updatedUserJson)
        .when()
            .put("/api/users/" + id)
        .then()
            .statusCode(400);
    }

    @Test
    public void testConflictUsername() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\" }";

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client");

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users/client")
        .then()
            .statusCode(409);
    }
}
