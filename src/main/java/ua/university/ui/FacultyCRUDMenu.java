package ua.university.ui;

import ua.university.domain.Faculty;
import ua.university.service.FacultyService;
import ua.university.ui.faculty.FacultyInputHandler;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.FacultyConsoleView;
import ua.university.util.Logging.ILogger;

import java.util.Scanner;

public class FacultyCRUDMenu {
    private final FacultyService facultyService;
    private final ILogger logger;
    private final Scanner scanner;
    private final FacultyConsoleView view;
    private final FacultyInputHandler inputHandler;

    public FacultyCRUDMenu(FacultyService facultyService, ILogger logger, Scanner scanner) {
        this.facultyService = facultyService;
        this.logger = logger;
        this.scanner = scanner;
        this.view = new FacultyConsoleView();
        this.inputHandler = new FacultyInputHandler(scanner);
    }

    public void createFaculty() {
        System.out.println("=== Create New Faculty ===");

        System.out.println("Enter Faculty Code:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        System.out.println("Enter Faculty Name:");
        String name = ConsoleInputValidator.readNonEmptyString(scanner);

        System.out.println("Enter Faculty Short Name:");
        String shortName = ConsoleInputValidator.readNonEmptyString(scanner);

        System.out.println("Enter Faculty Email:");
        String email = ConsoleInputValidator.readEmail(scanner);

        // Використовуємо окремий обробник для вводу даних декана (Teacher)
        var dean = inputHandler.readDeanData();
        Faculty faculty = new Faculty(code, name, shortName, dean, email); //

        try {
            facultyService.create(faculty); //
            logger.info("Faculty created successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void showFaculties() {
        var faculties = facultyService.findAll(); //
        if (faculties.isEmpty()) {
            logger.info("No faculties found.");
        } else {
            view.printList(faculties);
        }
    }

    public void deleteFaculty() {
        System.out.println("Enter Faculty Code to delete:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        try {
            facultyService.delete(code); //
            logger.info("Faculty deleted successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void updateFaculty() {
        System.out.println("Enter Faculty Code to update:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        var optionalFaculty = facultyService.findByCode(code); //
        if (optionalFaculty.isEmpty()) {
            logger.info("Faculty with code " + code + " not found.");
            return;
        }

        Faculty faculty = optionalFaculty.get();
        view.printDetails(faculty);
        System.out.println("Select property to update: 1-Name, 2-Short Name, 3-Contacts, 0-Exit");

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 3);
        switch (choice) {
            case 1 -> {
                System.out.println("Enter new faculty name:");
                facultyService.changeName(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Faculty name updated.");
            }
            case 2 -> {
                System.out.println("Enter new short name:");
                facultyService.changeShortName(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Short name updated.");
            }
            case 3 -> {
                System.out.println("Enter new contacts:");
                facultyService.changeContacts(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Contacts updated.");
            }
            default -> System.out.println("Update cancelled.");
        }
    }
}