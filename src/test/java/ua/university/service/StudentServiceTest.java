package ua.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.StudentNotFoundException;
import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.util.ConsoleInputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(new InMemoryStudentRepository());
    }

    @Test
    void createAndFindByIdShouldReturnStudent() {
        Student student = createTestStudent("1", "IPZ-2");

        studentService.create(student);

        assertTrue(studentService.findById("1").isPresent());
        // СТАЛО:
        assertEquals("S-1", studentService.findById("1").get().getStudentId());
    }

    @Test
    void createDuplicateShouldThrowCustomException() {
        Student student = createTestStudent("1",  "IPZ-2");
        studentService.create(student);

        assertThrows(DuplicateEntityException.class, () -> studentService.create(student));
    }

    @Test
    void getByIdForMissingStudentShouldThrowStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> studentService.getById("404"));
    }


    private Student createTestStudent(String number, String groupName) {
        return new Student(
                number,                // System ID
                "LastName" + number,   // Прізвище
                "FirstName" + number,  // Ім'я
                "MiddleName" + number, // По батькові
                LocalDate.of(2005, 1, 1), // Дата народження
                "test" + number + "@ukma.edu.ua", // Email
                "+3800000000" + number,           // Телефон
                "S-" + number,         // Student ID (номер заліковки)
                1,                     // Курс
                groupName,             // Група (використовується в тесті)
                2024,                  // Рік вступу
                StudyForm.BUDGET,      // Форма навчання
                StudentStatus.STUDYING // Статус
        );
    }

    // тест перевіряє коректність роботи фільтрації за підрядком у StudentService
    @Test
    void testFindByFullNamePartial() {
        // 1. Створюємо студента з прізвищем "Shevchenko"
        Student s = new Student("1", "Shevchenko", "Ivan", "P",
                LocalDate.of(2004, 1, 1),
                new Email("ivan@ukma.edu.ua"), new PhoneNumber("+380501112233"),
                "S1", null, null, 1, "IPZ-1", 2022, StudyForm.BUDGET, StudentStatus.STUDYING);

        studentService.create(s);

        // 2. Шукаємо частину прізвища
        List<Student> results = studentService.findByFullName("Shev");

        // 3. Перевіряємо, що список НЕ порожній
        assertFalse(results.isEmpty(), "Student should be found by partial name");
        assertEquals("Shevchenko", results.get(0).getLastName());
    }
    // Валідація формату групи
    @Test
    void testGroupFormatValidation() {
        // Емуляція вводу для Scanner
        Scanner scSuccess = new Scanner("ІПЗ-22\n");
        String result = ConsoleInputValidator.readGroup(scSuccess);
        assertEquals("ІПЗ-22", result);
    }

    @Test
    void testFindByGroupStream() {
        studentService.create(createTestStudent("1", "IPZ-1"));
        studentService.create(createTestStudent("2",  "ipz-1"));
        studentService.create(createTestStudent("3", "KN-2"));

        List<Student> result = studentService.findByGroup("IPZ-1");
        assertEquals(2, result.size());
    }

}
