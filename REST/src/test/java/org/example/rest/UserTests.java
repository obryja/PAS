package org.example.rest;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserTests extends BaseTest {
    /* &&&&&&&&&&&&&&&
            CRU
     &&&&&&&&&&&&&&&*/
    @Test
    public void testCreateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.CREATED.value())
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
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("username", equalTo("testclient"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testUpdateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
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
            .statusCode(HttpStatus.OK.value())
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
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("username", equalTo("diff"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testActivateAndDeactivateUser() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/users/" + id + "/deactivate")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("active", equalTo(false));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("active", equalTo(false));

        given()
            .contentType(ContentType.JSON)
        .when()
            .post("/api/users/" + id + "/activate")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("active", equalTo(true));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/" + id)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("active", equalTo(true));
    }

    @Test
    public void testGetAllUsers() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
            .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1))
            .body("[0].id", equalTo(id))
            .body("[0].username", equalTo("testclient"))
            .body("[0].active", equalTo(true))
            .body("[0].role", equalTo("ROLE_CLIENT"));
    }

    @Test
    public void testGetUserSByUsername() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
        .extract()
            .path("id");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/username/testclient")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(id))
            .body("username", equalTo("testclient"))
            .body("active", equalTo(true))
            .body("role", equalTo("ROLE_CLIENT"));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/api/users/username?username=test")
        .then()
            .statusCode(HttpStatus.OK.value())
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
        String userJson = "{ \"username\": \"d\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testBadRequestUpdate() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        String id =
        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
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
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testConflictUsername() {
        String userJson = "{ \"username\": \"testclient\", \"password\": \"test\", \"active\": \"true\", \"role\": \"ROLE_CLIENT\" }";

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users");

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/api/users")
        .then()
            .statusCode(HttpStatus.CONFLICT.value());
    }
}
