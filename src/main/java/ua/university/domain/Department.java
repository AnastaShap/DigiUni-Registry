package ua.university.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Department implements Serializable {

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

    // added  code to show
    @Override
    public String toString() {
        String facultyInfo = (faculty != null) ? faculty.getShortName() : "No Faculty";
        return String.format("Code: %-5s | Name: %-25s | Faculty: %-10s | Location: %s",
                code, name, facultyInfo, location);
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

    public Teacher getHead() {
        return head;
    }
}
