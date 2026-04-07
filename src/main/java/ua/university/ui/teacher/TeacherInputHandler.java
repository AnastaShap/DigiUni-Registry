package ua.university.ui.teacher;

import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.util.ConsoleInputValidator;
import java.time.LocalDate;
import java.util.Scanner;

public class TeacherInputHandler {
    private final Scanner scanner;

    public TeacherInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public LocalDate readDate(String prompt) {
        System.out.println(prompt + " (YYYY-MM-DD):");
        return LocalDate.parse(ConsoleInputValidator.readNonEmptyString(scanner));
    }

    public Email readEmail() {
        System.out.println("Enter Email:");
        return new Email(ConsoleInputValidator.readEmail(scanner));
    }

    public PhoneNumber readPhone() {
        System.out.println("Enter Phone:");
        return new PhoneNumber(ConsoleInputValidator.readPhone(scanner));
    }
}
