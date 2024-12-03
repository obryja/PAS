package org.example.rest.repositories;

import org.example.rest.models.User;

import java.util.List;

public interface UserRepository {
    User findById(String id);
    List<User> findAll();
    User create(User user);
    User update(User user);
    User findByUsername(String username);
    List<User> findByUsernameContaining(String partialUsername);
    List<User> findActiveClients();
}
