package ua.university.ui.student;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.Logging.ILogger;
import ua.university.util.StudentConsoleView;

import java.util.Optional;
import java.util.Scanner;

public class StudentCRUDMenu {
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final FacultyService facultyService;
    private final ILogger logger;
    private final Scanner scanner;
    private final StudentConsoleView view;

    private final StudentInputHandler inputHandler;
    private final StudentSearchAndReportManager searchManager;

    public StudentCRUDMenu(StudentService studentService,
                           DepartmentService departmentService,
                           FacultyService facultyService,
                           ILogger logger,
                           Scanner scanner) {
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.facultyService = facultyService;
        this.view = new StudentConsoleView(studentService::calculateAge); //

        this.logger = logger;
        this.scanner = scanner;

        this.inputHandler = new StudentInputHandler(scanner, view);
        this.searchManager = new StudentSearchAndReportManager(studentService, view, scanner);
    }

    public void createStudent() {
        view.printMessage("=== Create New Student ===");

        view.printMessage("Enter System ID (e.g., 101):");
        String id = ConsoleInputValidator.readNumericId(scanner);

        view.printMessage("Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        var bDate = inputHandler.readDate();

        // Wrap inputs into Record types to avoid primitive obsession
        view.printMessage("Email:");
        Email email = new Email(ConsoleInputValidator.readEmail(scanner));

        view.printMessage("Phone:");
        PhoneNumber phone = new PhoneNumber(ConsoleInputValidator.readPhone(scanner));

        view.printMessage("Course (1-6):");
        int course = ConsoleInputValidator.readCourse(scanner);

        view.printMessage("Group Name (e.g., IPZ-1):");
        String group = ConsoleInputValidator.readGroup(scanner);

        view.printMessage("Enter Student/Gradebook ID:");
        String sId = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Enter Faculty Code:");
        String faculCode = ConsoleInputValidator.readNonEmptyString(scanner);
        Optional<Faculty> faculOpt = facultyService.findByCode(faculCode);

        if (faculOpt.isEmpty()) {
            logger.info("Error: Faculty with code " + faculCode + " not found!");
            return;
        }
        Faculty faculty = faculOpt.get();

        view.printMessage("Enter Department Code:");
        String deptCode = ConsoleInputValidator.readNonEmptyString(scanner);
        Optional<Department> deptOptional = departmentService.findByCode(deptCode);

        if (deptOptional.isEmpty()) {
            logger.info("Error: Department with code " + deptCode + " not found!");
            return;
        }
        Department department = deptOptional.get();

        int entryYear = inputHandler.readInt("Entry Study Year:");
        var form = inputHandler.readStudyForm();
        var status = inputHandler.readStudentStatus();

        Student student = new Student(
                id, lastName, firstName, middleName,
                bDate, email, phone, sId, faculty,
                department,
                course, group, entryYear, form, status
        );

        try {
            studentService.create(student); //
            logger.info("Student created successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void showStudents() {
        searchManager.showAllStudentsMenu();
    }

    public void searchMenu() {
        searchManager.showSearchMenu();
    }

    public void deleteStudent() {
        view.printMessage("Enter System ID to delete:");
        String id = scanner.nextLine().trim();
        try {
            studentService.delete(id);
            logger.info("Student deleted successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void updateStudent() {
        view.printMessage("Enter System ID to update:");
        String id = scanner.nextLine().trim();
        var opt = studentService.findById(id);

        if (opt.isEmpty()) {
            logger.info("Student not found.");
            return;
        }

        Student s = opt.get();
        view.printDetails(s);
        view.printMessage("Select property to update: 1-Name, 2-Course, 3-Group, 4-Status, 0-Exit");

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 4);
        switch (choice) {
            case 1 -> {
                view.printMessage("Enter New Last Name:");
                String ln = ConsoleInputValidator.readNonEmptyString(scanner);
                view.printMessage("Enter New First Name:");
                String fn = ConsoleInputValidator.readNonEmptyString(scanner);
                view.printMessage("Enter New Middle Name:");
                String mn = ConsoleInputValidator.readNonEmptyString(scanner);
                studentService.changeName(s.getId(), ln, fn, mn);
                logger.info("Name updated.");
            }
            case 2 -> {
                studentService.changeCourse(s.getId(), ConsoleInputValidator.readCourse(scanner));
                logger.info("Course updated.");
            }
            case 3 -> {
                studentService.changeGroup(s.getId(), ConsoleInputValidator.readGroup(scanner));
                logger.info("Group updated.");
            }
            case 4 -> {
                s.setStatus(inputHandler.readStudentStatus());
                studentService.update(s);
                logger.info("Status updated.");
            }
            default -> view.printMessage("Update cancelled.");
        }
    }
}