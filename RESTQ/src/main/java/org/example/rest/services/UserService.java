package org.example.rest.services;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.example.rest.exceptions.BadRequestException;
import org.example.rest.exceptions.ConflictException;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.User;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    MongoClient mongoClient;

    public User getUserById(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        User user = userRepository.findById(id);

        if (user == null) {
            throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUserByUsername(String username) {
        if (username.length() < 3) {
            throw new BadRequestException("Nazwa użytkownika musi mieć przynajmniej 3 znaki");
        }

        return userRepository.findByUsername(username);
    }

    public List<User> getUsersByUsername(String username) {
        return new ArrayList<>(userRepository.findByUsernameContaining(username));
    }

    public User createUser(User user) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            User createdUser = userRepository.create(user);

            clientSession.commitTransaction();
            return createdUser;
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                throw new ConflictException("Ta nazwa użytkownika jest już zajęta.");
            } else {
                throw new RuntimeException("Wystąpił błąd podczas tworzenia użytkownika.", e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas tworzenia użytkownika.");
        }
    }

    public User updateUser(String id, String username, String password) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            User existingUser =  userRepository.findById(id);
            if (existingUser == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
            }

            existingUser.setUsername(username);
            existingUser.setPassword(password);

            User updatedUser = userRepository.update(existingUser);

            clientSession.commitTransaction();
            return updatedUser;
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                throw new ConflictException("Ta nazwa użytkownika jest już zajęta.");
            } else {
                throw new RuntimeException("Wystąpił błąd podczas tworzenia użytkownika.", e);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji użytkownika.");
        }
    }

    public User activateUser(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            User existingUser =  userRepository.findById(id);
            if (existingUser == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
            }

            existingUser.setActive(true);

            User updatedUser = userRepository.update(existingUser);

            clientSession.commitTransaction();
            return updatedUser;
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji użytkownika.");
        }
    }

    public User deactivateUser(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            User existingUser =  userRepository.findById(id);
            if (existingUser == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
            }

            existingUser.setActive(false);

            User updatedUser = userRepository.update(existingUser);

            clientSession.commitTransaction();
            return updatedUser;
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji użytkownika.");
        }
    }
}
