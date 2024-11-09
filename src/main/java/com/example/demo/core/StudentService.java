package com.example.demo.core;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.integration.BookingClient;
import com.example.demo.model.BookingRequest;
import com.example.demo.integration.ChuckClient;
import com.example.demo.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ChuckClient chuckClient;
    private final BookingClient bookingClient;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudent(Long studentId) {
//        try {
//            return studentRepository.findById(studentId);
//        } catch (Exception e) {
//            throw new StudentNotFoundException("Студент с id = " + studentId + " не был найден в базе данных");
//        }
        return Optional.ofNullable(studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Студент с id = " + studentId + " не был найден в базе данных"
                )));
    }

    public void addStudent(Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " taken");
        }
        student.setJoke(chuckClient.getJoke().getValue());
//        student.setBookingId(bookingClient.createBooking(new BookingRequest(
//                student.getName(),
//                "",
//                250,
//                true,
//                LocalDate.now(),
//                LocalDate.now(),
//                "wi-fi"
//        )));
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }
}
