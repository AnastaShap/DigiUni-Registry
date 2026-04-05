package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Teacher;
import ua.university.exception.DepartmentNotFoundException;
import ua.university.repository.IRepository;

import java.util.ArrayList;
import java.util.Optional;

public class TeacherService {
    private final IRepository<Teacher, String> teachers;

    public TeacherService(IRepository<Teacher, String> repository) {
        this.teachers = repository;
    }

   // private final List<Teacher> teachers = new ArrayList<>();

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public List<Teacher> getAll() {
        return List.copyOf(teachers);
    }

    public Optional<Teacher> findById(String id) {
        return teachers.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    private Department getOrThrow(String code) {
        return teachers.findById(code)
                .orElseThrow(() -> new DepartmentNotFoundException(code));
    }

}
