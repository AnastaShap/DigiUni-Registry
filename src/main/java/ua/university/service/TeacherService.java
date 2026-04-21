package ua.university.service;

import lombok.extern.slf4j.Slf4j;
import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.TeacherNotFoundException;
import ua.university.repository.IRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TeacherService {
    private final IRepository<Teacher, String> teacherRepository;

    public TeacherService(IRepository<Teacher, String> repository) {
        this.teacherRepository = repository;
    }

    public void create(Teacher teacher) {
        if (teacherRepository.findById(teacher.getId()).isPresent()) {
            log.warn("Attempt to create duplicate teacher: {}", teacher.getId());
            throw new DuplicateEntityException("Teacher with ID " + teacher.getId() + " already exists!");
        }
        teacherRepository.save(teacher);
        log.info("Teacher created: {} (ID: {})", teacher.getFullName(), teacher.getId());
    }

    public void update(Teacher teacher) {
        getOrThrow(teacher.getId());
        teacherRepository.save(teacher);
        log.info("Teacher updated: {}", teacher.getId());
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(String id) {
        return teacherRepository.findById(id);
    }

    public Teacher getById(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }

    public void delete(String id) {
        getOrThrow(id);
        teacherRepository.deleteById(id);
        log.info("Teacher deleted: {}", id);
    }

    private Teacher getOrThrow(String id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
    }
}