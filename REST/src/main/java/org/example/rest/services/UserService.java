package org.example.rest.services;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.example.rest.dto.UserCreateDTO;
import org.example.rest.dto.UserGetDTO;
import org.example.rest.dto.UserUpdateDTO;
import org.example.rest.exceptions.BadRequestException;
import org.example.rest.exceptions.ConflictException;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.Book;
import org.example.rest.models.User;
import org.example.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MongoClient mongoClient;

    public UserService(UserRepository userRepository, @Qualifier("mongoClient") MongoClient mongoClient) {
        this.userRepository = userRepository;
        this.mongoClient = mongoClient;
    }

    public UserGetDTO getUserById(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        User user = userRepository.findById(id);

        if (user == null) {
            throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
        }

        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }

    public List<UserGetDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole()))
                .collect(Collectors.toList());
    }

    public UserGetDTO getUserByUsername(String username) {
        if (username.length() < 3) {
            throw new BadRequestException("Nazwa użytkownika musi mieć przynajmniej 3 znaki");
        }

        User user = userRepository.findByUsername(username);
        return new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole());
    }

    public List<UserGetDTO> getUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username).stream()
                .map(user -> new UserGetDTO(user.getId(), user.getUsername(), user.isActive(), user.getRole()))
                .collect(Collectors.toList());
    }

    public User createUser(UserCreateDTO user) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            User newUser = User.createUser(user.getUsername(), user.getPassword(), user.isActive(), user.getRole());

            clientSession.startTransaction();

            User createdUser = userRepository.create(newUser);

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

    public User updateUser(String id, UserUpdateDTO changedUser) {
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

            existingUser.setUsername(changedUser.getUsername());
            existingUser.setPassword(changedUser.getPassword());

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

    public UserGetDTO activateUser(String id) {
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
            return new UserGetDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.isActive(), updatedUser.getRole());
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji użytkownika.");
        }
    }

    public UserGetDTO deactivateUser(String id) {
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
            return new UserGetDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.isActive(), updatedUser.getRole());
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji użytkownika.");
        }
    }
}
