package ua.university.service;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
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

    public void delete(String code) {
        if (departments.remove(code) == null) {
            throw new RuntimeException("Department not found");
        }
    }

    public void update(Department department) {
        Objects.requireNonNull(department);
        departments.put(department.getCode(), department);
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

    // ===== INTERNAL =====

    private Department getDepartmentOrThrow(String code) {
        return findByCode(code)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
