package ua.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Department;
import ua.university.domain.Student;
import ua.university.domain.Teacher;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.repository.InMemoryDepartmentRepository;
import ua.university.security.Permissions;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepartmentServiceTest {
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentService(new InMemoryDepartmentRepository());
    }
    // Перевірка додавання студента до кафедри
    @Test
    void testAddStudentToDepartment() {
        Department dep = new Department("INF", "Informatics", null, null, "1-st build");
        Student student = new Student("101", "Doe", "John", "M", LocalDate.of(2000, 1, 1),
                new Email("test@ukma.edu.ua"), new PhoneNumber("+380501234567"),
                "S-01", null, dep, 1, "IPZ-1", 2023, StudyForm.BUDGET, StudentStatus.STUDYING);


        departmentService.create(dep);
        departmentService.addStudent("INF", student);

        assertEquals(1, dep.getStudents().size());
        assertEquals("Doe John M", dep.getStudents().get(0).getFullName());
    }
}

