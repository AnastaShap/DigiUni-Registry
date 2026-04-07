package ua.university.dto;

import lombok.ToString;
import ua.university.exception.ValidationException;

/**
 * Record для Email з базовою перевіркою формату.
 */
public record Email(String value) {
    public Email {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format: " + value);
        }
    }
    @Override
    public String toString() {
        return value;
    }
}
