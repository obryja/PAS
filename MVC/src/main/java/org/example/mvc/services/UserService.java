package org.example.mvc.services;

import org.example.mvc.exceptions.UserAlreadyExistsException;
import org.example.mvc.models.UserCuDTO;
import org.example.mvc.models.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Value("${rest.api.url}")
    private String restApiUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createUser(UserCuDTO newUser) {
        try {
            restTemplate.postForEntity(restApiUrl + "/api/users/client", newUser, UserDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new UserAlreadyExistsException("User already exists");
            } else {
                throw new RuntimeException("Unexpected error: " + e.getMessage());
            }
        }
    }
}
