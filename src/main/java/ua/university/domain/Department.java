package ua.university.domain;
import java.util.ArrayList;
import java.util.List;

public class Department {

    private String code;
    private String name;
    private Faculty faculty;
    private Teacher head;
    private String location;
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();

    public Department(String code, String name, Faculty faculty,
                      Teacher head, String location) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.head = head;
        this.location = location;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public String getLocation() {
        return location;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public String toString() {
        return code + " - " + name + ", location: " + location;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }
}
