package org.example.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RentDetailsDTO {
    @NotNull
    private String id;

    @NotNull
    private String username;

    @NotNull
    private String title;

    @NotNull
    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    public RentDetailsDTO(String id, String username, String title, LocalDateTime beginDate, LocalDateTime endDate) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
