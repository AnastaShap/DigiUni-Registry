package ua.university.service;

import ua.university.domain.Teacher;
import ua.university.exception.TeacherNotFoundException; // Changed to TeacherNotFound
import ua.university.repository.IRepository;

import java.util.List;
import java.util.Optional;

public class TeacherService {
    private final IRepository<Teacher, String> teacherRepository;

    public TeacherService(IRepository<Teacher, String> repository) {
        this.teacherRepository = repository;
    }

    public void addTeacher(Teacher teacher) {
        teacherRepository.save(teacher); // Assuming your IRepository uses 'save' or 'add'
    }

    public List<Teacher> getAll() {
        // Use the repository's method to get all records
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(String id) {
        return teacherRepository.findById(id);
    }

    /**
     * Видаляє викладача за ідентифікатором.
     * Спочатку перевіряє, чи існує такий викладач.
     */
    public void delete(String id) {
        // Використовуємо твій getOrThrow для валідації
        getOrThrow(id);
        teacherRepository.deleteById(id);
    }
    /**
     * Helper method to get a teacher or throw an exception if not found.
     */
    private Teacher getOrThrow(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }
}