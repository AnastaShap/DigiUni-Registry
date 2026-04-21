package ua.university.dto;

import lombok.ToString;
import ua.university.exception.ValidationException;
import java.io.Serializable;

/**
 * Record для Email з базовою перевіркою формату.
 */
public record Email(String value) implements Serializable {
    public Email {
        if (value == null) throw new ValidationException("Email cannot be null");
        value = value.trim(); // Очищення від пробілів
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format: " + value);
        }
    }
    @Override
    public String toString() {
        return value;
    }
}