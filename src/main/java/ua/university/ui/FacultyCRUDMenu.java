package ua.university.ui;

import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import ua.university.service.FacultyService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;
import ua.university.util.StudentConsoleView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FacultyCRUDMenu {
    private final FacultyService facultyService;
    private final ILogger logger;
    private final Scanner scanner;
    private final StudentConsoleView view = new StudentConsoleView();

    public FacultyCRUDMenu(FacultyService facultyService, ILogger logger, Scanner scanner) {
        this.facultyService = facultyService;
        this.logger = logger;
        this.scanner = scanner;
    }

    public void createFaculty() {
        logger.info("=== Create New Faculty ===");

        logger.info("Enter Faculty Code:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Faculty Name:");
        String name = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Enter Faculty Short Name:");
        String shortName = ConsoleInputValidator.readNonEmptyString(scanner);

       /* view.printMessage("Enter Faculty Contacts:");
        String contacts = ConsoleInputValidator.readNonEmptyString(scanner);*/

        view.printMessage("Enter Faculty Email:");
        String contacts = ConsoleInputValidator.readEmail(scanner);  // ADD EMAIL CLASS (to avoid anti-pattern)

        Teacher dean = readDeanData();

        Faculty faculty = new Faculty(code, name, shortName, dean, contacts);
        //Faculty faculty = new Faculty(code, name, shortName, null, contacts);
       // facultyService.assignDean("");

        try {
            facultyService.create(faculty);
            logger.info("Faculty created successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void showFaculties() {
        List<Faculty> faculties = facultyService.findAll();
        if (faculties.isEmpty()) {
            logger.info("No faculties found.");
            return;
        }

        logger.info("=== Faculties ===");
        //faculties.forEach(f -> logger.info(f.toString()));

        // extended
        faculties.forEach(f -> {
            String output = String.format("Code: %-6s | Short: %-8s | Name: %-30s | Contacts: %s",
                    f.getCode(),
                    f.getShortName(),
                    f.getName(),
                    f.getContacts());
            logger.info(output);
        });
    }

    public void deleteFaculty() {
        logger.info("Enter Faculty Code to delete:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        try {
            facultyService.delete(code);
            logger.info("Faculty deleted successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void updateFaculty() {
        logger.info("Enter Faculty Code to update:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        Optional<Faculty> optionalFaculty = facultyService.findByCode(code);
        if (optionalFaculty.isEmpty()) {
            logger.info("Faculty not found.");
            return;
        }

        Faculty faculty = optionalFaculty.get();
        logger.info("Current faculty: " + faculty);
        logger.info("1-Name, 2-Short name, 3-Contacts, 0-Exit");

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 3);
        switch (choice) {
            case 1 -> {
                logger.info("Enter new faculty name:");
                facultyService.changeName(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Faculty updated.");
            }
            case 2 -> {
                logger.info("Enter new faculty short name:");
                facultyService.changeShortName(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Faculty updated.");
            }
            case 3 -> {
                logger.info("Enter new faculty contacts:");
                facultyService.changeContacts(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Faculty updated.");
            }
            default -> {
            }
        }
    }

    // helper :  create dean(teacher instance)

    private Teacher readDeanData() {
        logger.info("--- Entering Dean's Data ---");

        logger.info("Enter Teacher System ID:");
        String id = ConsoleInputValidator.readNumericId(scanner);

        logger.info("Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Email:");
        String email = ConsoleInputValidator.readNonEmptyString(scanner); // Можна використати readEmail

        logger.info("Phone:");
        String phone = ConsoleInputValidator.readPhone(scanner);

        logger.info("Position (e.g., Professor):");
        String position = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Degree (e.g., PhD):");
        String degree = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Academic Title:");
        String title = ConsoleInputValidator.readNonEmptyString(scanner);

        LocalDate birthDate = LocalDate.of(1980, 1, 1); // Тимчасово або через ввід
        LocalDate hireDate = LocalDate.now();
        double workload = 1.0;

        return new Teacher(
                id, lastName, firstName, middleName, birthDate,
                email, phone, position, degree, title, hireDate, workload
        );
    }
}
