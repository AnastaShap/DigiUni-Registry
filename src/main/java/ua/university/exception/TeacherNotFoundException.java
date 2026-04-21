package ua.university.exception;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String id) {
        super("Teacher not found: " + id);
    }
}
