package ua.university.ui;

import ua.university.domain.Faculty;
import ua.university.service.FacultyService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FacultyCRUDMenu {
    private final FacultyService facultyService;
    private final ILogger logger;
    private final Scanner scanner;

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

        logger.info("Enter Faculty Short Name:");
        String shortName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Faculty Contacts:");
        String contacts = ConsoleInputValidator.readNonEmptyString(scanner);

        Faculty faculty = new Faculty(code, name, shortName, null, contacts);

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
        faculties.forEach(f -> logger.info(f.toString()));
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
}
