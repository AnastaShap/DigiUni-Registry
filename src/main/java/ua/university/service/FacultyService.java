package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.exception.DuplicateEntityException;
import ua.university.exception.FacultyNotFoundException;

import java.util.*;

public class FacultyService {

    private final Map<String, Faculty> faculties = new HashMap<>();

    public void create(Faculty faculty) {
        Objects.requireNonNull(faculty);

        if (faculties.containsKey(faculty.getCode())) {
            throw new DuplicateEntityException("Faculty already exists: " + faculty.getCode());
        }

        faculties.put(faculty.getCode(), faculty);
    }

    public Optional<Faculty> findByCode(String code) {
        return Optional.ofNullable(faculties.get(code));
    }

    public List<Faculty> findAll() {
        return new ArrayList<>(faculties.values());
    }

    public void delete(String code) {
        if (faculties.remove(code) == null) {
            throw new FacultyNotFoundException(code);
        }
    }

    public void update(Faculty faculty) {
        Objects.requireNonNull(faculty);
        faculties.put(faculty.getCode(), faculty);
    }

    public void assignDean(String facultyCode, Teacher dean) {
        Faculty faculty = getFacultyOrThrow(facultyCode);
        faculty.setDean(dean);
    }

    public void addDepartment(String facultyCode, Department department) {
        Faculty faculty = getFacultyOrThrow(facultyCode);
        faculty.getDepartments().add(department);
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
