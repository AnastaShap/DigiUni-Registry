package ua.university.domain;
import java.time.LocalDate;
import java.util.Objects;

public class Teacher extends Person {
    private String position;
    private String degree;
    private String academicTitle;
    private LocalDate hireDate;
    private double workload;

    public Teacher(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, String email, String phone,
                   String position, String degree, String academicTitle,
                   LocalDate hireDate, double workload) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.workload = workload;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(position, teacher.position) &&
                Objects.equals(degree, teacher.degree) &&
                Objects.equals(academicTitle, teacher.academicTitle);
    }
    @Override
    public String toString() {
        return super.toString() +
                ", position=" + position +
                ", degree=" + degree +
                ", title=" + academicTitle +
                ", hireDate=" + hireDate +
                ", workload=" + workload;
    }
}

