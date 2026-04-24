package ua.university.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.repository.InMemoryDepartmentRepository;
import ua.university.repository.InMemoryFacultyRepository;
import ua.university.repository.student.InMemoryStudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SafeAdditionalTests {

    private StudentService studentService;
    private FacultyService facultyService;
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(new InMemoryStudentRepository());
        facultyService = new FacultyService(new InMemoryFacultyRepository());
        departmentService = new DepartmentService(new InMemoryDepartmentRepository());
    }

    @Test
    void findByCourseReturnsOnlyMatchingStudents() {
        studentService.create(createStudent("1", "Ivanenko", 1, "IPZ-1", 19));
        studentService.create(createStudent("2", "Petrenko", 2, "IPZ-2", 20));
        studentService.create(createStudent("3", "Shevchenko", 2, "KN-2", 21));

        List<Student> result = studentService.findByCourse(2);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> s.getCourse() == 2));
    }

    @Test
    void getStudentsSortedByCourseSortsAscending() {
        studentService.create(createStudent("1", "A", 3, "IPZ-3", 20));
        studentService.create(createStudent("2", "B", 1, "IPZ-1", 18));
        studentService.create(createStudent("3", "C", 2, "IPZ-2", 19));

        List<Student> result = studentService.getStudentsSortedByCourse();

        assertEquals(1, result.get(0).getCourse());
        assertEquals(2, result.get(1).getCourse());
        assertEquals(3, result.get(2).getCourse());
    }

    @Test
    void getStudentsSortedByNameSortsAlphabetically() {
        studentService.create(createStudent("1", "Zhuk", 1, "IPZ-1", 18));
        studentService.create(createStudent("2", "Bondar", 1, "IPZ-1", 18));
        studentService.create(createStudent("3", "Andriienko", 1, "IPZ-1", 18));

        List<Student> result = studentService.getStudentsSortedByName();

        assertEquals("Andriienko", result.get(0).getLastName());
        assertEquals("Bondar", result.get(1).getLastName());
        assertEquals("Zhuk", result.get(2).getLastName());
    }

    @Test
    void changeCourseUpdatesStudentInRepository() {
        studentService.create(createStudent("10", "Kovalenko", 1, "IPZ-1", 19));

        studentService.changeCourse("10", 3);

        Student updated = studentService.getById("10");
        assertEquals(3, updated.getCourse());
    }

    @Test
    void changeNameUpdatesAllNameFields() {
        studentService.create(createStudent("11", "OldLast", 2, "IPZ-2", 20));

        studentService.changeName("11", "NewLast", "NewFirst", "NewMiddle");

        Student updated = studentService.getById("11");
        assertEquals("NewLast", updated.getLastName());
        assertEquals("NewFirst", updated.getFirstName());
        assertEquals("NewMiddle", updated.getMiddleName());
    }

    @Test
    void findAdultsReturnsOnlyAdultStudents() {
        studentService.create(createStudent("20", "AdultOne", 2, "IPZ-2", 20));
        studentService.create(createStudent("21", "AdultTwo", 3, "IPZ-3", 19));
        studentService.create(createStudent("22", "MinorOne", 1, "IPZ-1", 17));

        List<Student> adults = studentService.findAdults();

        assertEquals(2, adults.size());
        assertTrue(adults.stream().allMatch(Student::isAdult));
    }

    @Test
    void getMostPopularCourseReturnsExpectedCourse() {
        studentService.create(createStudent("30", "A", 2, "IPZ-2", 20));
        studentService.create(createStudent("31", "B", 2, "KN-2", 21));
        studentService.create(createStudent("32", "C", 1, "IPZ-1", 19));

        Optional<Integer> result = studentService.getMostPopularCourse();

        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    void facultyAddDepartmentSetsBidirectionalLink() {
        Faculty faculty = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        Department department = new Department("INF", "Informatics", null, null, "1 корпус");

        facultyService.create(faculty);
        facultyService.addDepartment("FIT", department);

        assertEquals(1, faculty.getDepartments().size());
        assertEquals("INF", faculty.getDepartments().get(0).getCode());
        assertEquals(faculty, department.getFaculty());
    }

    @Test
    void facultyChangeContactsUpdatesFaculty() {
        Faculty faculty = new Faculty("FEN", "Faculty of Economics", "FEN", null, "old@ukma.edu.ua");
        facultyService.create(faculty);

        facultyService.changeContacts("FEN", "new@ukma.edu.ua");

        Faculty updated = facultyService.findByCode("FEN").orElseThrow();
        assertEquals("new@ukma.edu.ua", updated.getContacts());
    }

    @Test
    void departmentChangeLocationUpdatesDepartment() {
        Department department = new Department("MATH", "Mathematics", null, null, "old room");
        departmentService.create(department);

        departmentService.changeLocation("MATH", "new room");

        Department updated = departmentService.findByCode("MATH").orElseThrow();
        assertEquals("new room", updated.getLocation());
    }

    private Student createStudent(String id, String lastName, int course, String group, int age) {
        return new Student(
                id,
                lastName,
                "Ivan",
                "Petrovych",
                LocalDate.now().minusYears(age),
                "test" + id + "@ukma.edu.ua",
                "+3805012345" + String.format("%02d", Integer.parseInt(id)),
                "S-" + id,
                course,
                group,
                2022,
                StudyForm.BUDGET,
                StudentStatus.STUDYING
        );
    }
}
