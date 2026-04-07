package ua.university.repository;

import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTeacherRepository extends InMemoryRepository<Teacher, String> {
    public void save(Teacher teacher) {
        storage.put(teacher.getId(), teacher);
    }
}
