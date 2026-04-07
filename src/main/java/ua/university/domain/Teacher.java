package ua.university.domain;
import lombok.*;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public non-sealed class Teacher extends Person {
    private String position;
    private final String degree;
    private final String academicTitle;
    private final LocalDate hireDate;
    private double workload;
    private Faculty faculty;
    private Department department;

    public Teacher(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, Email email, PhoneNumber phone,
                   String position, String degree, String academicTitle,
                   LocalDate hireDate, double workload) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.workload = workload;
    }
   /* public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }*/

    /*@Override
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
    }*/



}

