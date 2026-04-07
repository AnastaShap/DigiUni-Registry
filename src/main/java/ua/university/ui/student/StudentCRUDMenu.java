package ua.university.ui.student;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;
import ua.university.util.StudentConsoleView;

import java.time.LocalDate;
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
        this.view = new StudentConsoleView(studentService::calculateAge);

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

        view.printMessage("Email:");
        String email = ConsoleInputValidator.readEmail(scanner);
        view.printMessage("Phone:");
        String phone = ConsoleInputValidator.readPhone(scanner);

        view.printMessage("Course Name:");
        int course = ConsoleInputValidator.readCourse(scanner);

        view.printMessage("Group Name:");
        String group = ConsoleInputValidator.readGroup(scanner);

        view.printMessage("Enter Student ID: ");
        String sId = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Enter Student Faculty: ");
        String faculCode = ConsoleInputValidator.readNonEmptyString(scanner);
        Optional<Faculty> faculOpt = facultyService.findByCode(faculCode);
        Faculty faculty = faculOpt.get();

        /// TO-DO: Transfer Department logic
        view.printMessage("Enter Department Code (e.g., CS-01):");
        String deptCode = ConsoleInputValidator.readNonEmptyString(scanner);

        // Шукаємо кафедру через сервіс
        Optional<Department> deptOptional = departmentService.findByCode(deptCode);

        if (deptOptional.isEmpty()) {
            logger.info("Error: Department with code " + deptCode + " not found!");
            return; // Зупиняємо створення, якщо кафедри не існує
        }


        int entryYear = inputHandler.readInt("Entry Study Year (e.g., 2024): ");
        var form = inputHandler.readStudyForm();
        var status = inputHandler.readStudentStatus();

        Department department = deptOptional.get();

        Student student = new Student(
                id, lastName, firstName, middleName,
                bDate, email, phone, sId, faculty,
                department,
                course, group, entryYear, form, status
        );
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
        view.printMessage("1-Name, 2-Course, 3-Group, 4-Status, 0-Exit"); // TO-DO: phone and email...

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

    // --- KT-3 SHOW STATISTICS TEST STREAM API

    public void showStatistics() {
        logger.info("=== Statistics ===");

        var byCourse = studentService.countByCourse();
        byCourse.forEach((course, count) ->
                logger.info("Course " + course + ": " + count)
        );

        double avgAge = studentService.getAverageAge();
        logger.info("Average age: " + avgAge);

        studentService.getLargestGroup()
                .ifPresent(e ->
                        logger.info("Largest group: " + e.getKey() + " (" + e.getValue() + ")")
                );
    }
}