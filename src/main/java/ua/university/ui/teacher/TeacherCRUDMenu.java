package ua.university.ui.teacher;

import lombok.AllArgsConstructor;
import ua.university.domain.Person;
import ua.university.domain.Teacher;
import ua.university.security.AuthService;
import ua.university.security.Permissions;
import ua.university.security.RequiresPermission;
import ua.university.service.TeacherService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.Logging.ILogger;
import ua.university.util.TeacherConsoleView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TeacherCRUDMenu {
    private final TeacherService teacherService;
    private final ILogger logger;
    private final Scanner scanner;
    private final AuthService authService;

    // Ці поля краще ініціалізувати всередині, щоб не "засмічувати" MainMenu
    private final TeacherConsoleView view;
    private final TeacherInputHandler inputHandler;

    public TeacherCRUDMenu(TeacherService teacherService,
                           ILogger logger,
                           Scanner scanner,
                           AuthService authService) {
        this.teacherService = teacherService;
        this.logger = logger;
        this.scanner = scanner;
        this.authService = authService;

        // Створюємо внутрішні залежності тут
        this.view = new TeacherConsoleView();
        this.inputHandler = new TeacherInputHandler(scanner);
    }

    @RequiresPermission(Permissions.EDIT_DATA)
    public void createTeacher() {
        Person currentUser = authService.getCurrentUser();
        if (!authService.canExecute(this, "createTeacher", currentUser)) {
            logger.info("Access Denied: You do not have permission to perform this action.");
            return;
        }

        logger.info("=== Create New Teacher ===");

        logger.info("Enter Teacher ID (numeric only):");
        String id = ConsoleInputValidator.readNumericId(scanner);

        logger.info("Enter Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        // Метод inputHandler.readDate вже приймає рядок повідомлення
        LocalDate birthDate = inputHandler.readDate("Enter Birth Date (YYYY-MM-DD):");

        logger.info("Enter Email Address:");
        var email = inputHandler.readEmail();

        logger.info("Enter Phone Number:");
        var phone = inputHandler.readPhone();

        logger.info("Enter Position (e.g., Professor, Lecturer):");
        String position = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Scientific Degree (e.g., PhD, Dr. Sc.):");
        String degree = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Academic Title (e.g., Associate Professor):");
        String title = ConsoleInputValidator.readNonEmptyString(scanner);

        LocalDate hireDate = inputHandler.readDate("Enter Hire Date (YYYY-MM-DD):");

        logger.info("Enter Workload (e.g., 1.0 for full-time, 0.5 for half-time):");
        // Додано обробку помилки формату числа для надійності
        double workload;
        try {
            workload = Double.parseDouble(ConsoleInputValidator.readNonEmptyString(scanner));
        } catch (NumberFormatException e) {
            logger.info("Invalid workload format. Defaulting to 1.0");
            workload = 1.0;
        }

        Teacher teacher = new Teacher(id, lastName, firstName, middleName, birthDate,
                email, phone, position, degree, title, hireDate, workload);

        try {
            teacherService.create(teacher);
            logger.info("Teacher " + teacher.getFullName() + " added successfully.");
        } catch (Exception e) {
            logger.info("Failed to create teacher: " + e.getMessage());
        }
    }

    public void showTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        if (teachers.isEmpty()) {
            logger.info("No teachers found.");
            return;
        }
        view.printList(teachers);
    }

    @RequiresPermission(Permissions.DELETE_DATA)
    public void deleteTeacher() {
        if (!authService.checkAccess(this, "deleteTeacher", logger)) return;

        logger.info("Enter Teacher ID to delete:");
        String id = scanner.nextLine().trim();
        try {
            teacherService.delete(id);
            logger.info("Teacher deleted.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }
}