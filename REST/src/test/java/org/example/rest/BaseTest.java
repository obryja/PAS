package org.example.rest;

import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {
    @Autowired
    private MongoDatabase mongoDatabase;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @BeforeEach
    public void setUp() {
        mongoDatabase.getCollection("books").drop();
        mongoDatabase.getCollection("users").drop();
        mongoDatabase.getCollection("rents").drop();
    }
}
