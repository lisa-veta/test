package com.example.demo;

import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8080)
@ActiveProfiles("test")
class UnhappyPathServiceTest {

    @Autowired
    private TestRestTemplate testTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void error500FromChuckNorrisServiceTest() {
        ChuckResponse chuckResponse = new ChuckResponse("Случайная шутка");
        // arrange
        stubFor(WireMock.get("/jokes/random")
                .willReturn(status(500)));

        // act
        Student student = new Student("Иван", "test@gmail.com", Gender.MALE);
        testTemplate.postForEntity("/api/v1/students", student, void.class);
        Student extractedStudent = testTemplate.getForObject("/api/v1/students/1", Student.class);

        // asserts
        assertEquals(chuckResponse.getValue(), extractedStudent.getJoke());
    }
}