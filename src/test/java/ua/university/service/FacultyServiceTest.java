package ua.university.service;

import org.junit.jupiter.api.Test;
import ua.university.domain.Faculty;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    @Test
    void duplicateFacultyShouldThrowCustomException() {
        FacultyService facultyService = new FacultyService();
        Faculty faculty = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");

        facultyService.create(faculty);

        assertThrows(DuplicateEntityException.class, () -> facultyService.create(faculty));
    }

    @Test
    void deletingMissingFacultyShouldThrowFacultyNotFoundException() {
        FacultyService facultyService = new FacultyService();

        assertThrows(FacultyNotFoundException.class, () -> facultyService.delete("MISSING"));
    }
}
