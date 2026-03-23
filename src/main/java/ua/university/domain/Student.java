package ua.university.domain;

import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;

import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person {
    private final String studentId;
    private Department department;
    private Faculty faculty;
    private int course;       // 1-6
    private String group;     // назва групи, String
    private int entryYear;
    private StudyForm studyForm;
    private StudentStatus status;

    public Student(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, String email, String phone,
                   String studentId, Faculty faculty, Department department, int course, String group,
                   int entryYear, StudyForm studyForm, StudentStatus status) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.studentId = studentId;
        this.faculty = faculty;
        this.department = department; // Link to department
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.studyForm = studyForm;
        this.status = status;
    }

    public Student(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, String email, String phone,
                   String studentId, int course, String group,
                   int entryYear, StudyForm studyForm, StudentStatus status) {
        this(id, lastName, firstName, middleName, birthDate, email, phone,
                studentId, null, null, course, group, entryYear, studyForm, status);
    }

    public String getStudentId() {
        return studentId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        if (course < 1 || course > 6) throw new IllegalArgumentException("Course must be 1-6");
        this.course = course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty (Faculty faculty) {
        this.faculty = faculty;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public StudyForm getStudyForm() {
        return studyForm;
    }

    public void setStudyForm(StudyForm studyForm) {
        this.studyForm = studyForm;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public boolean isAdult(Student s) {
        return s.getBirthDate()
                .isBefore(LocalDate.now().minusYears(18));
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Student s = (Student) o;
        return course == s.course &&
                entryYear == s.entryYear &&
                Objects.equals(group, s.group) &&
                studyForm == s.studyForm &&
                status == s.status &&
                Objects.equals(studentId, s.studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", course=" + course +
                ", faculty=" + faculty +
                ", group='" + group + '\'' +
                ", entryYear=" + entryYear +
                ", studyForm=" + studyForm +
                ", status=" + status +
                '}';
    }
}