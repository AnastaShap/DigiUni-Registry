package ua.university.ui.faculty;

import ua.university.domain.Teacher;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.util.ConsoleInputValidator;
import java.time.LocalDate;
import java.util.Scanner;

public class FacultyInputHandler {
    private final Scanner scanner;

    public FacultyInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public Teacher readDeanData() {
        System.out.println("--- Entering Dean's Data ---");
        System.out.println("Enter Teacher System ID:");
        String id = ConsoleInputValidator.readNumericId(scanner);

        System.out.println("Last Name:");
        String ln = ConsoleInputValidator.readNonEmptyString(scanner);
        System.out.println("First Name:");
        String fn = ConsoleInputValidator.readNonEmptyString(scanner);
        System.out.println("Middle Name:");
        String mn = ConsoleInputValidator.readNonEmptyString(scanner);

        System.out.println("Email:");
        Email email = new Email(ConsoleInputValidator.readEmail(scanner));
        System.out.println("Phone:");
        PhoneNumber phone = new PhoneNumber(ConsoleInputValidator.readPhone(scanner));

        System.out.println("Position (e.g., Professor):");
        String pos = ConsoleInputValidator.readNonEmptyString(scanner);
        System.out.println("Degree (e.g., PhD):");
        String deg = ConsoleInputValidator.readNonEmptyString(scanner);
        System.out.println("Academic Title:");
        String title = ConsoleInputValidator.readNonEmptyString(scanner);

        return new Teacher(id, ln, fn, mn, LocalDate.of(1980, 1, 1),
                email, phone, pos, deg, title, LocalDate.now(), 1.0); //
    }
}
