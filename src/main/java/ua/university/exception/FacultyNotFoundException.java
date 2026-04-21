package ua.university.exception;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(String code) {
        super("Faculty not found: " + code);
    }
}
