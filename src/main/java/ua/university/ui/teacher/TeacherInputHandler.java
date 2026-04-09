package ua.university.ui.teacher;

import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.util.ConsoleInputValidator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TeacherInputHandler {
    private final Scanner scanner;

    public TeacherInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.println(prompt + " (YYYY-MM-DD):");
            try {
                return LocalDate.parse(ConsoleInputValidator.readNonEmptyString(scanner));
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    public Email readEmail() {
        return new Email(ConsoleInputValidator.readEmail(scanner));
    }

    public PhoneNumber readPhone() {
        return new PhoneNumber(ConsoleInputValidator.readPhone(scanner));
    }
}