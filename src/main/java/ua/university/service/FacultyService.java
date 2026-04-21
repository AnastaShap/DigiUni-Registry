package ua.university.service;

import lombok.extern.slf4j.Slf4j;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;
import ua.university.repository.IRepository;

import java.util.*;

@Slf4j
public class FacultyService {

    private final IRepository<Faculty, String> repository;

    public FacultyService(IRepository<Faculty, String> repository) {
        this.repository = repository;
    }

    private Faculty getOrThrow(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new FacultyNotFoundException(code));
    }

    public void create(Faculty faculty) {
        Objects.requireNonNull(faculty);
        if (repository.findById(faculty.getCode()).isPresent()) {
            log.error("Faculty {} already exists", faculty.getCode());
            throw new DuplicateEntityException("Faculty already exists: " + faculty.getCode());
        }
        repository.save(faculty);
        log.info("Faculty created: {}", faculty.getShortName());
    }

    public void update(Faculty faculty) {
        Objects.requireNonNull(faculty);
        repository.save(faculty);
        log.info("Faculty updated: {}", faculty.getCode());
    }

    public void delete(String code) {
        getOrThrow(code);
        repository.deleteById(code);
        log.info("Faculty deleted: {}", code);
    }

    public List<Faculty> findAll() {
        return repository.findAll();
    }

    public Optional<Faculty> findByCode(String code) {
        return repository.findById(code);
    }

    public void assignDean(String facultyCode, Teacher dean) {
        getOrThrow(facultyCode).setDean(dean);
    }

    public void addDepartment(String facultyCode, Department department) {
        Faculty faculty = getOrThrow(facultyCode);
        faculty.getDepartments().add(department);
        department.setFaculty(faculty); // ПРАВКА: Встановлюємо зворотний зв'язок
        log.info("Department {} linked to faculty {}", department.getCode(), facultyCode);
    }

    public void changeName(String facultyCode, String newName) {
        getOrThrow(facultyCode).setName(newName);
    }

    public void changeShortName(String facultyCode, String newShortName) {
        getOrThrow(facultyCode).setShortName(newShortName);
    }

    public void changeContacts(String facultyCode, String newContacts) {
        getOrThrow(facultyCode).setContacts(newContacts);
    }
}