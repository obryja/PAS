package org.example.rest.models;

import jakarta.validation.constraints.NotBlank;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.*;

public class Book {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    @BsonProperty("title")
    @NotBlank(message = "Tytuł nie może być pusty")
    private String title;

    @BsonCreator
    public Book(@BsonProperty("title") String title) {
        this.title = title;
    }

    public Book() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + "]";
    }
}
