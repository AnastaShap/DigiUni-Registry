package ua.university.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
public final class Student extends Person implements Serializable { //
    private final String studentId;
    private Department department;
    private Faculty faculty;
    private int course;       // 1-6
    private String group;
    private int entryYear;
    private StudyForm studyForm;
    private StudentStatus status;

    // Головний конструктор, що приймає об'єкти DTO
    public Student(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, Email email, PhoneNumber phone,
                   String studentId, Faculty faculty, Department department,
                   int course, String group, int entryYear,
                   StudyForm studyForm, StudentStatus status) {
        // Виправлено синтаксис: просто передаємо об'єкти, а не оголошуємо їх типи
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.studentId = studentId;
        this.faculty = faculty;
        this.department = department;
        this.course = course;
        this.group = group;
        this.entryYear = entryYear;
        this.studyForm = studyForm;
        this.status = status;
    }

    // Допоміжний конструктор для вводу рядків (наприклад, з MainMenu)
    public Student(String id, String lastName, String firstName, String middleName,
                   LocalDate birthDate, String email, String phone,
                   String studentId, int course, String group,
                   int entryYear, StudyForm studyForm, StudentStatus status) {
        // Виправлено: рядки обгортаються в об'єкти Email та PhoneNumber перед передачею
        this(id, lastName, firstName, middleName, birthDate,
                new Email(email), new PhoneNumber(phone),
                studentId, null, null, course, group, entryYear, studyForm, status);
    }

    public int getAge() {
        return (getBirthDate() == null) ? 0 : Period.between(getBirthDate(), LocalDate.now()).getYears();
    }

    public boolean isAdult() {
        return getBirthDate() != null && getBirthDate().isBefore(LocalDate.now().minusYears(18));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        if (!super.equals(o)) return false;
        return course == student.course &&
                entryYear == student.entryYear &&
                Objects.equals(studentId, student.studentId) &&
                Objects.equals(group, student.group) &&
                studyForm == student.studyForm &&
                status == student.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), studentId, course, group, entryYear, studyForm, status);
    }
}