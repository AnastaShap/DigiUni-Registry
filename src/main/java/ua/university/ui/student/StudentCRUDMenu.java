package ua.university.ui.student;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;
import ua.university.util.StudentConsoleView;

import java.time.LocalDate;
import java.util.Scanner;

public class StudentCRUDMenu {
    private final StudentService studentService;
    private final ILogger logger;
    private final Scanner scanner;
    private final StudentConsoleView view = new StudentConsoleView();

    private final StudentInputHandler inputHandler;
    private final StudentSearchAndReportManager searchManager;

    public StudentCRUDMenu(StudentService studentService, ILogger logger, Scanner scanner) {
        this.studentService = studentService;
        this.logger = logger;
        this.scanner = scanner;
        this.inputHandler = new StudentInputHandler(scanner, view);
        this.searchManager = new StudentSearchAndReportManager(studentService, view, scanner);
    }

    public void createStudent() {
        view.printMessage("=== Create New Student ===");

        view.printMessage("Enter System ID (e.g., 101):");
        String id = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        var bDate = inputHandler.readDate();

        view.printMessage("Email:");
        String email = scanner.nextLine();
        view.printMessage("Phone:");
        String phone = scanner.nextLine();

        view.printMessage("Course Name:");
        int course = ConsoleInputValidator.readCourse(scanner);

        view.printMessage("Group Name:");
        String group = ConsoleInputValidator.readGroup(scanner);

        view.printMessage("Enter Student ID: ");
        String sId = ConsoleInputValidator.readNonEmptyString(scanner);
        //view.printMessage("Enter Department ID: ");
        String deptId = ConsoleInputValidator.readNonEmptyString(scanner);

        int year = inputHandler.readInt("Entry Study Year (e.g., 2024): ");

        var form = inputHandler.readStudyForm();
        var status = inputHandler.readStudentStatus();

        Student student = new Student(id, lastName, firstName, middleName, bDate, email, phone,
                sId, course, group, year, form, status);
        try {
            studentService.create(student);
            logger.info("Student created successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void showStudents() { searchManager.showAllStudentsMenu(); }

    public void searchMenu() { searchManager.showSearchMenu(); }

    public void deleteStudent() {
        view.printMessage("Enter System ID to delete:");
        studentService.delete(scanner.nextLine());
        logger.info("Deleted successfully.");
    }

    public void updateStudent() {
        view.printMessage("Enter System ID to update:");
        String id = scanner.nextLine();
        var opt = studentService.findById(id);

        if (opt.isEmpty()) {
            logger.info("Not found.");
            return;
        }

        Student s = opt.get();
        view.printDetails(s);
        view.printMessage("1-Name, 2-Course, 3-Group, 4-Status, 0-Exit");

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 4);
        switch (choice) {
            case 1 -> {
                String ln = ConsoleInputValidator.readNonEmptyString(scanner);
                String fn = ConsoleInputValidator.readNonEmptyString(scanner);
                String mn = ConsoleInputValidator.readNonEmptyString(scanner);
                studentService.changeName(s.getId(), ln, fn, mn);
            }
            case 2 -> studentService.changeCourse(s.getId(), ConsoleInputValidator.readCourse(scanner));
            case 3 -> studentService.changeGroup(s.getId(), ConsoleInputValidator.readGroup(scanner));
            case 4 -> {
                s.setStatus(inputHandler.readStudentStatus());
                studentService.update(s);
            }
        }
        logger.info("Updated.");
    }
}