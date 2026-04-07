package ua.university.repository.student;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.repository.InMemoryRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// In-memory реалізація
public class InMemoryStudentRepository extends InMemoryRepository<Student, String> implements IStudentRepository {

    @Override
    public void save(Student student) {
        // Записуємо дані безпосередньо в потокобезпечну ConcurrentHashMap батька
        storage.put(student.getId(), student);
    }
}