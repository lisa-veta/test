package com.example.demo.integration;

import com.example.demo.config.BookingProperties;
import com.example.demo.config.ChuckProperties;
import com.example.demo.model.BookingRequest;
import com.example.demo.model.BookingResponse;
import com.example.demo.model.ChuckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookingClientTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookingProperties properties = new BookingProperties();

    private BookingClient bookingClient;

    @BeforeEach
    public void setUp() {
        bookingClient = new BookingClient(properties, restTemplate);
    }
    @Test
    public void getBookingIdSuccess() {
        BookingRequest request =  new BookingRequest(
                "name",
                "lastname",
                100,
                true,
                LocalDate.now(),
                LocalDate.now(),
                "wi-fi");
        // Arrange
        BookingResponse expectedResponse = new BookingResponse(100, request);
        String url = properties.getUrl();

        when(restTemplate.exchange(
                eq(url + "/booking"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BookingResponse.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        // Act
        int bookId = bookingClient.createBooking(request);

        // Assert
        assertEquals(expectedResponse.getBookingId(), bookId);
    }

    @Test
    public void getBookingIdServerError() {
        // Arrange
        String url = properties.getUrl() + "/booking"; // Исправленный URL
        int expectedId = -1;
        BookingRequest request = new BookingRequest();

        when(restTemplate.exchange(
                eq(url), // Исправленный URL
                eq(HttpMethod.POST), // Исправленный метод
                any(HttpEntity.class), // Исправленная проверка
                eq(BookingResponse.class)
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        int actualResponse = bookingClient.createBooking(request);

        // Assert
        assertEquals(expectedId, actualResponse);
    }
}