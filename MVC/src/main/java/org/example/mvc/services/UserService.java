package org.example.mvc.services;

import org.example.mvc.exceptions.ConflictException;
import org.example.mvc.models.UserCuDTO;
import org.example.mvc.models.UserDTO;
import org.example.mvc.models.UserGetDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Value("${rest.api.url}")
    private String restApiUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UserGetDTO> getActiveUsers() {
        try {
            ResponseEntity<UserGetDTO[]> response = restTemplate.getForEntity(restApiUrl + "/api/users/active/client", UserGetDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createUser(UserCuDTO newUser) {
        try {
            restTemplate.postForEntity(restApiUrl + "/api/users/client", newUser, UserDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ConflictException(e.getMessage());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
