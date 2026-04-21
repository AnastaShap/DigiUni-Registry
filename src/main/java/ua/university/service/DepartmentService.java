package ua.university.service;

import lombok.extern.slf4j.Slf4j;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;
import ua.university.exception.DepartmentNotFoundException;
import ua.university.exception.DuplicateEntityException;
import ua.university.repository.IRepository;

import java.util.*;

@Slf4j
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
        repository.save(department);
        log.info("Department updated: {}", department.getCode());
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
        Department dep = getOrThrow(departmentCode);
        dep.getStudents().add(student);
        student.setDepartment(dep); // ПРАВКА: Встановлюємо зворотний зв'язок
        log.info("Student {} added to department {}", student.getId(), departmentCode);
    }

    public void assignHead(String departmentCode, Teacher teacher) {
        getOrThrow(departmentCode).setHead(teacher);
        log.info("Teacher {} assigned as head of department {}", teacher.getId(), departmentCode);
    }

    public void changeName(String departmentCode, String newName) {
        getOrThrow(departmentCode).setName(newName);
    }

    public void changeLocation(String departmentCode, String newLocation) {
        getOrThrow(departmentCode).setLocation(newLocation);
    }

    public void changeFaculty(String departmentCode, Faculty faculty) {
        getOrThrow(departmentCode).setFaculty(faculty);
    }
}
