package ua.university.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
public final class Teacher extends Person {
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
        // Тепер приймає об'єкти замість String
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.position = position;
        this.degree = degree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.workload = workload;
    }
}