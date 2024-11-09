package com.example.demo.integration;

import com.example.demo.config.BookingProperties;
import com.example.demo.model.BookingRequest;
import com.example.demo.model.BookingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BookingClient {

    private final BookingProperties properties;
    private final RestTemplate restTemplate;

    public int createBooking(BookingRequest request) {
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.5993.117 Safari/537.36");

            HttpEntity<BookingRequest> requestEntity = new HttpEntity<>(request);
            ResponseEntity<BookingResponse> responseEntity = restTemplate.exchange(
                    properties.getUrl() + "/booking",
                    HttpMethod.POST,
                    requestEntity,
                    BookingResponse.class
            );

            return responseEntity.getBody().getBookingId();
        } catch (HttpServerErrorException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            return -1;
        }
    }
}
