package ua.university.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String code) {
        super("Department not found: " + code);
    }
}
