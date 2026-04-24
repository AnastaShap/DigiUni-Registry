package ua.university.repository;

import ua.university.domain.Student;
import ua.university.domain.Teacher;

public class InMemoryTeacherRepository extends InMemoryRepository<Teacher, String> {

    @Override
    public void save(Teacher teacher) {
        // Додаємо вчителя в мапу storage, використовуючи його ID як ключ
        storage.put(teacher.getId(), teacher);
    }
}
