package org.example.rest.models;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import java.time.LocalDateTime;

public class Rent {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    @BsonProperty("userId")
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String userId;

    @BsonProperty("bookId")
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String bookId;

    @BsonProperty("beginDate")
    private LocalDateTime beginDate;

    @BsonProperty("endDate")
    private LocalDateTime endDate;

    @BsonCreator
    public Rent(@BsonProperty("userId") String userId, @BsonProperty("bookId") String bookId, @BsonProperty("beginDate") LocalDateTime beginDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.beginDate = beginDate;
    }

    public Rent() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", userId=" + userId + ", bookId=" + bookId + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
    }
}
