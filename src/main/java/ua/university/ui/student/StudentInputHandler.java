package ua.university.ui.student;

import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.StudentConsoleView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class StudentInputHandler {
    private final Scanner scanner;
    private final StudentConsoleView view;

    public StudentInputHandler(Scanner scanner, StudentConsoleView view) {
        this.scanner = scanner;
        this.view = view;
    }

    // New logic for Date
    public LocalDate readDate() {
        while (true) {
            view.printMessage("Birth Date (YYYY-MM-DD):");
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                view.printMessage("Invalid format. Use YYYY-MM-DD.");
            }
        }
    }

    // Logic for StudyForm Enum
    public StudyForm readStudyForm() {
        view.printMessage("Select Study Form: 1 - BUDGET, 2 - CONTRACT");
        int choice = ConsoleInputValidator.readMenuOption(scanner, 1, 2);
        return choice == 1 ? StudyForm.BUDGET : StudyForm.CONTRACT;
    }

    // Logic for StudentStatus Enum
    public StudentStatus readStudentStatus() {
        view.printMessage("Select Status: 1-STUDYING, 2-GRADUATED, 3-ACADEMIC_LEAVE, 4-EXPELLED");
        int choice = ConsoleInputValidator.readMenuOption(scanner, 1, 4);
        return switch (choice) {
            case 1 -> StudentStatus.STUDYING;
            case 2 -> StudentStatus.GRADUATED;
            case 3 -> StudentStatus.ACADEMIC_LEAVE;
            default -> StudentStatus.EXPELLED;
        };
    }

    // Simple helper for years/numbers not covered by specific validators
    public int readInt(String prompt) {
        view.printMessage(prompt);
        return ConsoleInputValidator.intInRange(scanner, 1900, 2100);
    }
}