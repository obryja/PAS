package org.example.mvc.services;

import org.example.mvc.exceptions.ConflictException;
import org.example.mvc.exceptions.NotFoundException;
import org.example.mvc.models.BookDTO;
import org.example.mvc.models.RentCreateDTO;
import org.example.mvc.models.RentDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RentService {
    private final RestTemplate restTemplate;

    @Value("${rest.api.url}")
    private String restApiUrl;

    public RentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createRent(RentCreateDTO newRent) {
        try {
            restTemplate.postForEntity(restApiUrl + "/api/rents", newRent, RentCreateDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ConflictException(e.getResponseBodyAsString());
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException(e.getResponseBodyAsString());
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public List<RentDetailsDTO> getRents() {
        try {
            ResponseEntity<RentDetailsDTO[]> response = restTemplate.getForEntity(restApiUrl + "/api/rents/details", RentDetailsDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void endRent(String id) {
        try {
            restTemplate.postForLocation(restApiUrl + "/api/rents/" + id, null);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteRent(String id) {
        try {
            restTemplate.delete(restApiUrl + "/api/rents/" + id);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
