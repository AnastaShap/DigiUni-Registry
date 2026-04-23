package ua.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;
import ua.university.repository.IRepository;
import ua.university.repository.InMemoryFacultyRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    private FacultyService facultyService;

    @BeforeEach
    void setUp() {
        // Ініціалізація перед кожним тестом
        IRepository<Faculty, String> repo = new InMemoryFacultyRepository();
        facultyService = new FacultyService(repo);
    }

    @Test
    void duplicateFacultyShouldThrowCustomException() {
        Faculty faculty = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(faculty);

        // Перевірка на викидання власного винятку
        assertThrows(DuplicateEntityException.class, () -> facultyService.create(faculty));
    }

    @Test
    void deletingMissingFacultyShouldThrowFacultyNotFoundException() {
        // Перевірка використання Optional та Custom Exception
        assertThrows(FacultyNotFoundException.class, () -> facultyService.delete("MISSING"));
    }

    // oбробка дублікатів факультетів
    @Test
    void testCreateDuplicateFacultyThrowsException() {
        Faculty f = new Faculty("FIT", "IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(f);

        assertThrows(RuntimeException.class, () -> facultyService.create(f),
                "Should throw exception if code already exists");
    }

    // Тест перевіряє оновлення об'єкта Dean у моделі Faculty
    @Test
    void testAssignDean() {
        Faculty fit = new Faculty("FIT", "IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(fit);
        Teacher dean = new Teacher("D1", "Glybovets", "A", "M", LocalDate.now(), null, null, "P", "D", "T", LocalDate.now(), 1.0);

        facultyService.assignDean("FIT", dean);

        assertTrue(fit.getDean().isPresent());
        assertEquals("Glybovets", fit.getDean().get().getLastName());
    }
}

