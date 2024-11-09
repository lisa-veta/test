package com.example.demo.integration;

import com.example.demo.config.BookingProperties;
import com.example.demo.config.ChuckProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
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

    }
    @Test
    void createBooking() {
    }
}