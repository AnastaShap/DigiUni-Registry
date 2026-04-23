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
            logger.info("Access Denied.");
            return;
        }

        logger.info("=== Create New Teacher ===");

        String id = ConsoleInputValidator.readNumericId(scanner);
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        LocalDate birthDate = inputHandler.readDate("Enter Birth Date");
        var email = inputHandler.readEmail();
        var phone = inputHandler.readPhone();

        logger.info("Enter Position:");
        String position = ConsoleInputValidator.readNonEmptyString(scanner);
        logger.info("Enter Degree:");
        String degree = ConsoleInputValidator.readNonEmptyString(scanner);
        logger.info("Enter Academic Title:");
        String title = ConsoleInputValidator.readNonEmptyString(scanner);

        LocalDate hireDate = inputHandler.readDate("Enter Hire Date");

        logger.info("Enter Workload (e.g., 1.0):");
        double workload = Double.parseDouble(ConsoleInputValidator.readNonEmptyString(scanner));

        Teacher teacher = new Teacher(id, lastName, firstName, middleName, birthDate,
                email, phone, position, degree, title, hireDate, workload);

        try {
            teacherService.create(teacher);
            logger.info("Teacher added successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
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
        if (!authService.checkAccess(this, "deleteStudent", logger)) return;

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