package org.example.rest.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "rents")
public class Rent {
    @Id
    private String id;

    private String userId;
    private String bookId;
    private Date beginDate;
    private Date endDate;

    public Rent(String userId, String bookId, Date beginDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.beginDate = beginDate;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
