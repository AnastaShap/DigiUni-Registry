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
import java.util.Map;

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
    @Test
    void averageAgeByGroupCalculatesAverage() {
        studentService.create(createStudent("301", "А", 2, "ІПЗ-2", 20));
        studentService.create(createStudent("302", "Б", 2, "ІПЗ-2", 22));

        Map<String, Double> stats = studentService.averageAgeByGroup();

        assertEquals(21.0, stats.get("ІПЗ-2"));
    }

    @Test
    void countStudentsByCourseReturnsGroupedStats() {
        studentService.create(createStudent("201", "А", 1, "ІПЗ-1", 18));
        studentService.create(createStudent("202", "Б", 2, "ІПЗ-2", 19));
        studentService.create(createStudent("203", "В", 2, "КН-2", 20));

        Map<Integer, Long> stats = studentService.countStudentsByCourse();

        assertEquals(1L, stats.get(1));
        assertEquals(2L, stats.get(2));
    }

    private Student createStudent(String id, String lastName, int course, String group, int age) {
        return new Student(
                id,
                lastName,
                "Іван",
                "Петрович",
                LocalDate.now().minusYears(age),
                id + "@ukma.edu.ua",
                "+380501234567",
                "S" + id,
                course,
                group,
                2022,
                StudyForm.BUDGET,
                StudentStatus.STUDYING
        );
    }
}
