package ua.university.service;

import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.TeacherNotFoundException;
import ua.university.repository.IRepository;

import java.util.List;
import java.util.Optional;

public class TeacherService {
    private final IRepository<Teacher, String> teacherRepository;

    public TeacherService(IRepository<Teacher, String> repository) {
        this.teacherRepository = repository;
    }

    /**
     * Створює нового викладача.
     * @throws DuplicateEntityException якщо ID вже зайнятий
     */
    public void create(Teacher teacher) {
        if (teacherRepository.findById(teacher.getId()).isPresent()) {
            throw new DuplicateEntityException("Teacher with ID " + teacher.getId() + " already exists!");
        }
        teacherRepository.save(teacher);
    }

    /**
     * Оновлює дані існуючого викладача.
     */
    public void update(Teacher teacher) {
        getOrThrow(teacher.getId()); // Перевірка на існування
        teacherRepository.save(teacher);
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(String id) {
        return teacherRepository.findById(id);
    }

    /**
     * Повертає викладача або кидає виняток, якщо його не знайдено.
     */
    public Teacher getById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

    /**
     * Видаляє викладача за ідентифікатором.
     */
    public void delete(String id) {
        getOrThrow(id); // Валідація перед видаленням
        teacherRepository.deleteById(id);
    }

    /**
     * Допоміжний метод для перевірки існування.
     */
    private Teacher getOrThrow(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }
}