package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;
import ua.university.repository.IRepository;

import java.util.*;

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
            throw new DuplicateEntityException("Faculty already exists: " + faculty.getCode());
        }
        repository.save(faculty);
    }

    public void update(Faculty faculty) {
        Objects.requireNonNull(faculty);
        repository.save(faculty);
    }

    public void delete(String code) {
        getOrThrow(code);
        repository.deleteById(code);
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
        getOrThrow(facultyCode).getDepartments().add(department);
    }

    public void changeName(String facultyCode, String newName) {
        Faculty faculty = getFacultyOrThrow(facultyCode);
        faculty.setName(newName);
    }

    public void changeShortName(String facultyCode, String newShortName) {
        Faculty faculty = getFacultyOrThrow(facultyCode);
        faculty.setShortName(newShortName);
    }

    public void changeContacts(String facultyCode, String newContacts) {
        Faculty faculty = getFacultyOrThrow(facultyCode);
        faculty.setContacts(newContacts);
    }

    private Faculty getFacultyOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new FacultyNotFoundException(code));
    }
}
