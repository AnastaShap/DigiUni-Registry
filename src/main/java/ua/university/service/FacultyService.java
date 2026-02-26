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

    // ===== INTERNAL =====

    private Faculty getFacultyOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
    }
}