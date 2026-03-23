package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Teacher;
import ua.university.repository.IRepository;

public class TeacherService {
    private final IRepository<Teacher, String> repository;

    public TeacherService(IRepository<Teacher, String> repository) {
        this.repository = repository;
    }


}
