package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Student;
import ua.university.domain.Teacher;

import java.util.*;

public class DepartmentService {

    private final Map<String, Department> departments = new HashMap<>();

    // ===== CREATE =====

    public void create(Department department) {
        Objects.requireNonNull(department);

        departments.put(department.getCode(), department);
    }

    // ===== FIND =====

    public Optional<Department> findByCode(String code) {
        return Optional.ofNullable(departments.get(code));
    }

    public List<Department> findAll() {
        return new ArrayList<>(departments.values());
    }

    // ===== BUSINESS =====

    public void addStudent(String departmentCode, Student student) {

        Department dep = getDepartmentOrThrow(departmentCode);

        dep.getStudents().add(student);
    }

    public void addTeacher(String departmentCode, Teacher teacher) {

        Department dep = getDepartmentOrThrow(departmentCode);

        dep.getTeachers().add(teacher);
    }

    public void assignHead(String departmentCode, Teacher teacher) {

        Department dep = getDepartmentOrThrow(departmentCode);

        dep.setHead(teacher);
    }

    // ===== INTERNAL =====

    private Department getDepartmentOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
