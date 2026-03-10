package ua.university.repository.student;

import ua.university.domain.Student;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи зі студентами.
 * <p>
 * Визначає базові операції доступу до даних.
 * </p>
 */
public interface IStudentRepository {

    List<Student> findAll();

    Optional<Student> findById(String id);

    void save(Student student);

    void deleteById(String id);

    //oid updateById(Student student);
}
