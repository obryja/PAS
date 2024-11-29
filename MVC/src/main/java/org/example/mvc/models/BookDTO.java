package org.example.mvc.models;

import jakarta.validation.constraints.NotBlank;

public class BookDTO {
    private String id;

    @NotBlank
    private String title;

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
}
