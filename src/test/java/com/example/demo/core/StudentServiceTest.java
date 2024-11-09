package com.example.demo.core;

import com.example.demo.config.ChuckProperties;
import com.example.demo.core.StudentRepository;
import com.example.demo.core.StudentService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.integration.BookingClient;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.ChuckResponse;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentServiceTest {
    @Autowired
    private StudentRepository studentRepository;
    private StudentService studentService;
    @MockBean
    private ChuckClient chuckClient;
    @MockBean
    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        studentService = new StudentService(studentRepository, chuckClient, bookingClient);
    }
    @Test
    void getAllStudents() {
        List<Student> expectedStudents = Arrays.asList(
                new Student(1L, "Иван", "test1@gmail.com", null, null, 0),
                new Student(2L, "Петр", "test2@gmail.com", null, null, 0)
        );

        when(studentRepository.findAll()).thenReturn(expectedStudents);

        List<Student> actualStudents = studentService.getAllStudents();

        verify(studentRepository, times(1)).findAll();

        assertEquals(expectedStudents, actualStudents);
    }
    @Test
    void getExistStudent(){
        Long studentId = 1L;
        Student student = new Student(studentId, "ivan", "test@gmail.com", Gender.MALE, "", 0);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Student actualStudent = studentService.getStudent(studentId).get();
        assertEquals(student, actualStudent);
    }

    @Test
    void getNotExistStudent() {
        Long studentId = 1L;
        StudentNotFoundException ex = assertThrows(StudentNotFoundException.class, () -> studentService.getStudent(studentId));
        assertEquals("Студент с id = " + studentId + " не был найден в базе данных", ex.getMessage());
    }

    @Test
    void addStudentWithNotExistingEmail() {
        Student student = new Student(1L, "Иван", "test@gmail.com", null, null, 0);
        ChuckResponse jokeResponse = new ChuckResponse("шутка");

        when(chuckClient.getJoke()).thenReturn(jokeResponse);

        when(bookingClient.createBooking(any())).thenReturn(123);
        when(studentRepository.selectExistsEmail(student.getEmail())).thenReturn(false);

        studentService.addStudent(student);

        verify(studentRepository, times(1)).save(student);
        verify(chuckClient, times(1)).getJoke();
        verify(bookingClient, times(1)).createBooking(any());
        assertEquals("шутка", student.getJoke());
        assertEquals(123, student.getBookingId());
    }

    @Test
    void addStudentWithExistingEmailThrowsBadRequestException() {
        Student student = new Student(1L, "Иван", "test@gmail.com", null, null, 0);
        when(studentRepository.selectExistsEmail(student.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> studentService.addStudent(student));

        verify(studentRepository, never()).save(any());
        verify(chuckClient, never()).getJoke();
        verify(bookingClient, never()).createBooking(any());
    }

    @Test
    void deleteExistingStudent() {
        Long studentId = 1L;

        when(studentRepository.existsById(studentId)).thenReturn(true);

        studentService.deleteStudent(studentId);

        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void deleteNonExistingStudent() {
        Long studentId = 1L;

        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(studentId));

        verify(studentRepository, never()).deleteById(studentId);
    }
}
