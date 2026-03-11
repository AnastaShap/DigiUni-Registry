package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;
import ua.university.exception.DepartmentNotFoundException;
import ua.university.exception.DuplicateEntityException;
import ua.university.repository.IRepository;

import java.util.*;

public class DepartmentService {

    private final IRepository<Department, String> repository;

    public DepartmentService(IRepository<Department, String> repository) {
        this.repository = repository;
    }

    private Department getOrThrow(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new DepartmentNotFoundException(code));
    }

    public void create(Department department) {
        if (repository.findById(department.getCode()).isPresent()) {
            throw new DuplicateEntityException("Department exists: " + department.getCode());
        }
        repository.save(department);
    }

    public void update(Department department) {
        Objects.requireNonNull(department);
        repository.save(department); // Тепер через репозиторій
    }

    public void delete(String code) {
        getOrThrow(code); // Перевірка на існування
        repository.deleteById(code);
    }

    public Optional<Department> findByCode(String code) {
        return repository.findById(code);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    // Методи бізнес-логіки
    public void addStudent(String departmentCode, Student student) {
        getOrThrow(departmentCode).getStudents().add(student);
    }

    public void assignHead(String departmentCode, Teacher teacher) {
        getOrThrow(departmentCode).setHead(teacher);
    }

    public void changeName(String departmentCode, String newName) {
        Department dep = getDepartmentOrThrow(departmentCode);
        dep.setName(newName);
    }

    public void changeLocation(String departmentCode, String newLocation) {
        Department dep = getDepartmentOrThrow(departmentCode);
        dep.setLocation(newLocation);
    }

    public void changeFaculty(String departmentCode, Faculty faculty) {
        Department dep = getDepartmentOrThrow(departmentCode);
        dep.setFaculty(faculty);
    }

    private Department getDepartmentOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new DepartmentNotFoundException(code));
    }
}
