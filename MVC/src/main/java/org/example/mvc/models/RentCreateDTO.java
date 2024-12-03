package org.example.mvc.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RentCreateDTO {
    @NotNull
    private String userId;

    @NotNull
    private String bookId;

    @NotNull
    private LocalDateTime beginDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }
}
