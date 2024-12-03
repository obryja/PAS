package org.example.mvc.services;

import org.example.mvc.models.BookDTO;
import org.example.mvc.models.UserGetDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    private final RestTemplate restTemplate;

    @Value("${rest.api.url}")
    private String restApiUrl;

    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookDTO> getAvailableBooks() {
        try {
            ResponseEntity<BookDTO[]> response = restTemplate.getForEntity(restApiUrl + "/api/books/available", BookDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
