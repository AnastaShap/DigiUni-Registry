package ua.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Faculty;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;
import ua.university.repository.IRepository;
import ua.university.repository.InMemoryFacultyRepository;

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
}

//    @Test
//    void duplicateFacultyShouldThrowCustomException() {
//        FacultyService facultyService = new FacultyService();
//        Faculty faculty = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
//
//        facultyService.create(faculty);
//
//        assertThrows(DuplicateEntityException.class, () -> facultyService.create(faculty));
//    }
//
//    @Test
//    void deletingMissingFacultyShouldThrowFacultyNotFoundException() {
//        FacultyService facultyService = new FacultyService();
//
//        assertThrows(FacultyNotFoundException.class, () -> facultyService.delete("MISSING"));
//    }

