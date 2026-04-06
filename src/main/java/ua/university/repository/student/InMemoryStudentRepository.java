package ua.university.repository.student;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// In-memory реалізація
public class InMemoryStudentRepository implements IStudentRepository {

    private final List<Student> students = new ArrayList<>();

    @Override
    public List<Student> findAll() {
        return List.copyOf(students);
    }

    @Override
    public Optional<Student> findById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    @Override
    public void save(Student student) {
        Objects.requireNonNull(student, "Student cannot be null");
        deleteById(student.getId());
        students.add(student);
    }

    @Override
    public void deleteById(String id) {
        students.removeIf(s -> s.getId().equals(id));
    }

    // testing data
    public void loadTestData(InMemoryStudentRepository repository) {
        repository.save(new Student(
                "1", "Шевченко", "Іван", "Петрович",
                LocalDate.of(2003, 5, 10),
                "ivan@ukma.edu.ua", "0500000001",
                "S001", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        ));

        repository.save(new Student(
                "2", "Коваленко", "Анна", "Олегівна",
                LocalDate.of(2004, 3, 20),
                "anna@ukma.edu.ua", "0500000002",
                "S002", 1, "AВІС-1",
                2023, StudyForm.CONTRACT, StudentStatus.STUDYING
        ));

        repository.save(new Student(
                "3", "Бондар", "Максим", "Ігорович",
                LocalDate.of(2002, 11, 2),
                "max@ukma.edu.ua", "0500000003",
                "S003", 2, "КН-2",
                2021, StudyForm.BUDGET, StudentStatus.STUDYING
        ));
    }
}