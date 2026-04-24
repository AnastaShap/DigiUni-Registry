package ua.university.io;
import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;

import java.io.Serializable;
import java.util.List;

public record UniversityDataSnapshot(
        List<Faculty> faculties,
        List<Department> departments,
        List<Student> students,
        List<Teacher> teachers) implements Serializable {
}