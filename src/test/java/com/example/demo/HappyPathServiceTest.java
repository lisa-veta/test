package com.example.demo;

import com.example.demo.config.ChuckProperties;
import com.example.demo.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8080)
@ActiveProfiles("test")
public class HappyPathServiceTest {

    @Value("chuck.url")
    private String url;

    @Autowired
    private TestRestTemplate testTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void happyPathServiceTest() throws JsonProcessingException {
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
        stubFor(WireMock.post("/booking")
                .willReturn(okJson(objectMapper.writeValueAsString(expectedResponse))));
        // arrange
        ChuckResponse chuckResponse = new ChuckResponse("Some random joke value");
        stubFor(WireMock.get("/jokes/random")
                .willReturn(okJson(objectMapper.writeValueAsString(chuckResponse))));

        // act
        Student student = new Student("ivan", "test@gmail.com", Gender.MALE);
        testTemplate.postForEntity("/api/v1/students", student, Void.class);
        Student extractedStudent = testTemplate.getForObject("/api/v1/students/1", Student.class);

        // asserts
        assertEquals("Some random joke value", extractedStudent.getJoke());
        assertEquals(100, extractedStudent.getBookingId());
    }

    //    @Test
//    public void happyPathBookingServiceTest() throws JsonProcessingException {
//        // arrange
//        BookingResponse bookingResponse = new BookingResponse();
//        bookingResponse.setBookingId(1);
//        stubFor(post("/booking")
//                .willReturn(okJson(objectMapper.writeValueAsString(bookingResponse))));
//
//        // act
//        BookingRequest request = new BookingRequest("John", "Doe", 100, true, LocalDate.now(), LocalDate.now(), "Wi-Fi");
//        int bookingId = bookingClient.createBooking(request);
//
//        // asserts
//        assertEquals(123, bookingId);
//    }

}
