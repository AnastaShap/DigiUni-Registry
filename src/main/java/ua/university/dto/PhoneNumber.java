package ua.university.dto;

import ua.university.exception.ValidationException;

import java.io.Serializable;

public record PhoneNumber(String value) implements Serializable {
    public PhoneNumber {
        // Приклад: перевірка, що номер починається з '+' або цифри та має певну довжину
        if (value == null || !value.matches("^\\+?[0-9]{10,13}$")) {
            throw new ValidationException("Invalid phone format: " + value);
        }
    }
    @Override
    public String toString() {
        return value;
    }
}