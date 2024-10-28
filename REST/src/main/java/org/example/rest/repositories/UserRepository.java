package org.example.rest.repositories;

import org.example.rest.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findByUsernameContaining(String partialUsername);
}