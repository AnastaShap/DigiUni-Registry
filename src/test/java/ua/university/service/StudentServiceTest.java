package ua.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.StudentNotFoundException;
import ua.university.repository.student.InMemoryStudentRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(new InMemoryStudentRepository());
    }

    @Test
    void createAndFindByIdShouldReturnStudent() {
        Student student = buildStudent("1", "S001", 2, "IPZ-2");

        studentService.create(student);

        assertTrue(studentService.findById("1").isPresent());
        assertEquals("S001", studentService.getById("1").getStudentId());
    }

    @Test
    void createDuplicateShouldThrowCustomException() {
        Student student = buildStudent("1", "S001", 2, "IPZ-2");
        studentService.create(student);

        assertThrows(DuplicateEntityException.class, () -> studentService.create(student));
    }

    @Test
    void getByIdForMissingStudentShouldThrowStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> studentService.getById("404"));
    }


    private Student buildStudent(String id, String studentId, int course, String group) {
        return new Student(
                id,
                "Петренко",
                "Данило",
                "Іванович",
                LocalDate.of(2004, 5, 10),
                studentId.toLowerCase() + "@ukma.edu.ua",
                "050111110000",
                studentId,
                course,
                group,
                2020,
                StudyForm.BUDGET,
                StudentStatus.STUDYING
        );
    }
}
