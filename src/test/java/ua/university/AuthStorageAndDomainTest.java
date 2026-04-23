package ua.university;
import org.junit.jupiter.api.Test;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.io.DataStorageService;
import ua.university.io.UniversityDataSnapshot;
import ua.university.repository.InMemoryTeacherRepository;
import ua.university.service.TeacherService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class AuthStorageAndDomainTest {

    @Test
    void dataStorageServiceSavesAndLoadsSnapshot() throws Exception {
        DataStorageService storageService = new DataStorageService();
        Path tempFile = Files.createTempFile("university-data", ".bin");

        Faculty faculty = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        Department department = new Department("INF", "Informatics", faculty, null, "1 корпус");
        Student student = new Student(
                "500",
                "Ткаченко",
                "Марія",
                "Іванівна",
                LocalDate.of(2004, 1, 10),
                "maria@ukma.edu.ua",
                "+380501111111",
                "S500",
                2,
                "ІПЗ-2",
                2022,
                StudyForm.BUDGET,
                StudentStatus.STUDYING
        );

        UniversityDataSnapshot snapshot = new UniversityDataSnapshot(
                List.of(faculty),
                List.of(department),
                List.of(student)
        );

        storageService.save(tempFile, snapshot);
        UniversityDataSnapshot loaded = storageService.load(tempFile);

        assertNotNull(loaded);
        assertEquals(1, loaded.faculties().size());
        assertEquals(1, loaded.departments().size());
        assertEquals(1, loaded.students().size());
        assertEquals("Ткаченко", loaded.students().get(0).getLastName());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void teacherServiceCreateStoresTeacherInRepository() {
        TeacherService teacherService = new TeacherService(new InMemoryTeacherRepository());
        Teacher teacher = new Teacher(
                "T-1",
                "Іваненко",
                "Олег",
                "Петрович",
                LocalDate.of(1985, 5, 5),
                new Email("teacher@ukma.edu.ua"),
                new PhoneNumber("+380501234567"),
                "Professor",
                "PhD",
                "Docent",
                LocalDate.of(2015, 9, 1),
                1.0
        );

        teacherService.create(teacher);

        assertEquals(1, teacherService.getAll().size());
        assertEquals("Іваненко", teacherService.getAll().get(0).getLastName());
    }


}