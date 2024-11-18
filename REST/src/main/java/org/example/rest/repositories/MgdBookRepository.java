package org.example.rest.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.InsertOneResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.rest.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MgdBookRepository implements BookRepository {
    private final MongoCollection<Book> bookCollection;

    @Autowired
    public MgdBookRepository(MongoDatabase mongoDatabase) {
        this.bookCollection = mongoDatabase.getCollection("books", Book.class);
    }

    @Override
    public Book findById(String id) {
        return bookCollection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        bookCollection.find().into(books);
        return books;
    }

    @Override
    public Book create(Book book) {
        InsertOneResult newBook = bookCollection.insertOne(book);
        return bookCollection.find(Filters.eq("_id", newBook.getInsertedId())).first();
    }

    @Override
    public Book update(Book book) {
        Bson filter = Filters.eq("_id", new ObjectId(book.getId()));
        ReplaceOptions options = new ReplaceOptions().upsert(false);
        if(bookCollection.replaceOne(filter, book, options).getModifiedCount() > 0) {
            return bookCollection.find(filter).first();
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        return bookCollection.deleteOne(Filters.eq("_id", new ObjectId(id))).getDeletedCount() > 0;
    }
}
