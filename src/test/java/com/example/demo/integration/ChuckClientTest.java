package com.example.demo.integration;

import com.example.demo.config.ChuckProperties;
import com.example.demo.model.ChuckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChuckClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ChuckProperties properties = new ChuckProperties();

    private ChuckClient chuckClient;

    @BeforeEach
    public void setUp() {
        chuckClient = new ChuckClient(restTemplate, properties);
    }
    @Test
    public void getJokeSuccess() {
        // Arrange
        ChuckResponse expectedResponse = new ChuckResponse("Смешная шутка");
        String url = properties.getUrl();

        when(restTemplate.exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        // Act
        ChuckResponse actualResponse = chuckClient.getJoke();

        // Assert
        assertEquals(expectedResponse.getValue(), actualResponse.getValue());
    }

    @Test
    public void getJokeServerError() {
        // Arrange
        String url = properties.getUrl();

        when(restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ChuckResponse>() {}
        )).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        // Act
        ChuckResponse actualResponse = chuckClient.getJoke();

        // Assert
        assertEquals("Случайная шутка", actualResponse.getValue());
    }
}