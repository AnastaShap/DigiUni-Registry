package ua.university;

import org.junit.jupiter.api.Test;
import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {

    @Test
    void testFullNameParsing() {
        // Тестуємо метод setFullName, який використовує split("\\s+")
        Student s = createMinimalStudent();
        s.setFullName("Шевченко Тарас Григорович");

        assertEquals("Шевченко", s.getLastName());
        assertEquals("Тарас", s.getFirstName());
        assertEquals("Григорович", s.getMiddleName());
    }

    @Test
    void testStudentAdultStatus() {
        // Студент, якому виповнилося 18 років
        Student adult = createStudentWithBirthDate(LocalDate.now().minusYears(19));
        Student minor = createStudentWithBirthDate(LocalDate.now().minusYears(17));

        assertTrue(adult.isAdult(), "Student over 18 should be adult");
        assertFalse(minor.isAdult(), "Student under 18 should not be adult");
    }

    @Test
    void testInvalidPersonConstructor() {
        // Перевірка валідації в конструкторі
        assertThrows(IllegalArgumentException.class, () -> {
            // Передаємо валідні пошту і телефон, щоб перевірка дійшла саме до помилки ID
            new Student(null, "L", "F", "M", LocalDate.now(),
                    "test@ukma.edu.ua", "+380501234567",
                    "S1", 1, "G1", 2022, StudyForm.BUDGET, StudentStatus.STUDYING);
        }, "ID cannot be empty");
    }

    // Допоміжні методи для створення об'єктів
    private Student createMinimalStudent() {
        return new Student("ID1", "L", "F", "M", LocalDate.of(2000, 1, 1),
                "test@ukma.edu.ua", "+380501234567",
                "S1", 1, "G1", 2022, StudyForm.BUDGET, StudentStatus.STUDYING);
    }

    private Student createStudentWithBirthDate(LocalDate date) {
        return new Student("ID1", "L", "F", "M", date,
                "test@ukma.edu.ua", "+380501234567",
                "S1", 1, "G1", 2022, StudyForm.BUDGET, StudentStatus.STUDYING);
    }
}