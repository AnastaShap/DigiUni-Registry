package ua.university.domain;
import lombok.*;
import lombok.ToString;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor // Потрібно для деяких бібліотек серіалізації
public abstract sealed class Person permits Student, Teacher {
    private String id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private Email email;
    private PhoneNumber phone;

    protected Person(String id, String lastName, String firstName, String middleName,
                     LocalDate birthDate, Email email, PhoneNumber phone) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }

    public void setFullName(String fullName) {
        String[] parts = fullName == null ? new String[0] : fullName.trim().split("\\s+");
        if (parts.length > 0) this.lastName = parts[0];
        if (parts.length > 1) this.firstName = parts[1];
        if (parts.length > 2) this.middleName = parts[2];
    }
}
/*
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}*/