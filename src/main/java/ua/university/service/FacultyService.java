package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Teacher;

import java.util.*;

public class FacultyService {

    // use Map instead of List because of complexity O(1)
    private final Map<String, Faculty> faculties = new HashMap<>();

    // ===== CREATE =====

    public void create(Faculty faculty) {
        Objects.requireNonNull(faculty);

        if (faculties.containsKey(faculty.getCode())) {
            throw new RuntimeException("Faculty already exists");
        }

        faculties.put(faculty.getCode(), faculty);
    }

    // ===== FIND =====

    public Optional<Faculty> findByCode(String code) {
        return Optional.ofNullable(faculties.get(code));
    }

    public List<Faculty> findAll() {
        return new ArrayList<>(faculties.values());
    }

    public void delete(String code) {
        if (faculties.remove(code) == null) {
            throw new RuntimeException("Faculty not found");
        }
    }

    public void update(Faculty faculty) {
        Objects.requireNonNull(faculty);
        faculties.put(faculty.getCode(), faculty);
    }

    // ===== BUSINESS LOGIC =====

    public void assignDean(String facultyCode, Teacher dean) {

        Faculty faculty = getFacultyOrThrow(facultyCode);

        // бізнес правило можна додати пізніше
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

    // ===== INTERNAL =====

    private Faculty getFacultyOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }
}