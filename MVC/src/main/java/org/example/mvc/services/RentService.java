package org.example.mvc.services;

import org.example.mvc.exceptions.UserAlreadyExistsException;
import org.example.mvc.models.RentDTO;
import org.example.mvc.models.UserCuDTO;
import org.example.mvc.models.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RentService {
    private final RestTemplate restTemplate;

    @Value("${rest.api.url}")
    private String restApiUrl;

    public RentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createRent(RentDTO newRent) {
        try {
            restTemplate.postForEntity(restApiUrl + "/api/rents", newRent, RentDTO.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*public List<RentDTO> getAllRents() {
        List<RentDTO> rents = Arrays.asList(restTemplate.getForObject(rentsUrl, RentDTO[].class));
        List<RentDTO> rentDtos = new ArrayList<>();

        for (RentDTO rent : rents) {
            User user = restTemplate.getForObject(usersUrl + "/" + rent.getUserId(), User.class);
            Book book = restTemplate.getForObject(booksUrl + "/" + rent.getBookId(), Book.class);

            RentDTO rentDto = new RentDTO();
            rentDto.setId(rent.getId());
            rentDto.setUsername(user.getUsername());
            rentDto.setBookTitle(book.getTitle());
            rentDto.setBeginDate(rent.getBeginDate());
            rentDto.setEndDate(rent.getEndDate());

            rentDtos.add(rentDto);
        }

        return rentDtos;
    }*/

    /*public List<RentDTO> getAllRents() {
        ResponseEntity<List<RentDTO>> response = restTemplate.exchange(
                apiBaseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RentDTO>>() {}
        );
        return response.getBody();
    }

    public void endRent(String id) {
        String url = apiBaseUrl + "/api/rents/" + id;
        restTemplate.postForLocation(url, null);
    }*/
}
