package ua.university.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.university.domain.Teacher;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.service.TeacherService;
import ua.university.ui.teacher.TeacherInputHandler;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.Logging.ILogger;
import ua.university.util.TeacherConsoleView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Data
@AllArgsConstructor
public class TeacherCRUDMenu {
    private final TeacherService teacherService;
    private final ILogger logger;
    private final Scanner scanner;
    private final TeacherConsoleView view;
    private final TeacherInputHandler inputHandler;

    public void createTeacher() {
        logger.info("=== Create New Teacher ===");

        logger.info("Enter ID:");
        String id = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        // Припускаємо, що ConsoleInputValidator має методи для роботи з датами та числами
        // Якщо ні, можна використати LocalDate.parse(scanner.nextLine())
        logger.info("Enter Birth Date (YYYY-MM-DD):");
        LocalDate birthDate = LocalDate.parse(ConsoleInputValidator.readNonEmptyString(scanner));

        logger.info("Enter Email:");
        Email email = new Email(ConsoleInputValidator.readNonEmptyString(scanner));

        logger.info("Enter Phone:");
        PhoneNumber phone = new PhoneNumber(ConsoleInputValidator.readNonEmptyString(scanner));

        logger.info("Enter Position (e.g., Professor, Associate Professor):");
        String position = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Degree (e.g., PhD):");
        String degree = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Academic Title:");
        String academicTitle = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Hire Date (YYYY-MM-DD):");
        LocalDate hireDate = LocalDate.parse(ConsoleInputValidator.readNonEmptyString(scanner));

        logger.info("Enter Workload (e.g., 1.0):");
        // parsing
        double workload = Double.parseDouble(ConsoleInputValidator.readNonEmptyString(scanner));

        Teacher teacher = new Teacher(
                id, lastName, firstName, middleName, birthDate,
                email, phone, position, degree, academicTitle,
                hireDate, workload
        );

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

        logger.info("=== Teachers List ===");
        teachers.forEach(t -> {
            String output = String.format("ID: %-5s | Name: %-25s | Position: %-15s | Workload: %.1f",
                    t.getId(),
                    t.getLastName() + " " + t.getFirstName().charAt(0) + ".",
                    t.getPosition(),
                    // Припускаємо наявність getter-а для workload або використання toString()
                    1.0); // Заглушка, якщо немає прямого доступу
            logger.info(t.toString());
        });
    }

    public void findTeacherById() {
        logger.info("Enter Teacher ID to find:");
        String id = ConsoleInputValidator.readNonEmptyString(scanner);

        Optional<Teacher> teacher = teacherService.findById(id);
        if (teacher.isPresent()) {
            logger.info("Teacher found: " + teacher.get());
        } else {
            logger.info("Teacher with ID " + id + " not found.");
        }
    }

    public void deleteTeacher() {
        showTeachers();
        logger.info("Enter Teacher ID to delete:");
        String id = ConsoleInputValidator.readNonEmptyString(scanner);

        try {
            teacherService.delete(id);
            logger.info("Delete functionality should be implemented in TeacherService.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }
}
