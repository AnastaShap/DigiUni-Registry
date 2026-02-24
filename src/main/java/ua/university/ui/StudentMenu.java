package ua.university.ui;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;
import ua.university.util.StudentConsoleView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StudentMenu {

    private final StudentService studentService;
    private final ILogger logger;
    private final StudentConsoleView view = new StudentConsoleView();
    private final Scanner scanner;

    public StudentMenu(StudentService studentService,
                       ILogger logger,
                       Scanner scanner) {
        this.studentService = studentService;
        this.logger = logger;
        this.scanner = scanner;
    }

    // ================= CREATE =================

    public void createStudent() {
        view.printMessage("=== Create New Student ===");

        view.printMessage("Enter ID:");
        String id = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        // Date handling
        LocalDate birthDate = null;
        while (birthDate == null) {
            view.printMessage("Birth Date (YYYY-MM-DD):");
            try {
                birthDate = LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                view.printMessage("Invalid format. Please use YYYY-MM-DD.");
            }
        }

        view.printMessage("Email:");
        String email = scanner.nextLine();

        view.printMessage("Phone:");
        String phone = scanner.nextLine();

        view.printMessage("Student Ticket ID:");
        String studentTicketId = scanner.nextLine();

        int course = ConsoleInputValidator.readCourse(scanner);
        String group = ConsoleInputValidator.readGroup(scanner);

        view.printMessage("Entry Year (e.g., 2022):");
        int entryYear = Integer.parseInt(scanner.nextLine());

        // Default values for enums (can be expanded to selection menus later)
        StudyForm studyForm = StudyForm.BUDGET;
        StudentStatus status = StudentStatus.STUDYING;

        Student newStudent = new Student(
                id, lastName, firstName, middleName,
                birthDate, email, phone, studentTicketId,
                course, group, entryYear, studyForm, status
        );

        try {
            studentService.create(newStudent);
            logger.info("Student created successfully.");
        } catch (RuntimeException e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    // ================= DELETE =================

    public void deleteStudent() {
        view.printMessage("Enter ID to delete:");
        String id = scanner.nextLine();

        try {
            studentService.delete(id);
            logger.info("Student deleted successfully.");
        } catch (RuntimeException e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    // ================= UPDATE =================

    public void updateStudent() {
        view.printMessage("Enter student ID to update:");
        String id = scanner.nextLine();

        Optional<Student> optionalStudent = studentService.findById(id);

        if (optionalStudent.isEmpty()) {
            logger.info("Student with ID " + id + " not found.");
            return;
        }

        Student student = optionalStudent.get();
        view.printDetails(student);

        view.printMessage("""
                Select field to update:
                1 - Name (Last/First/Middle)
                2 - Course
                3 - Group
                0 - Cancel
                """);

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 3);

        switch (choice) {
            case 1 -> updateName(student);
            case 2 -> {
                int newCourse = ConsoleInputValidator.readCourse(scanner);
                studentService.changeCourse(student.getId(), newCourse);
            }
            case 3 -> {
                String newGroup = ConsoleInputValidator.readGroup(scanner);
                studentService.changeGroup(student.getId(), newGroup);
            }
            case 0 -> {
                return;
            }
        }

        logger.info("Student updated successfully.");
    }

    private void updateName(Student student) {
        view.printMessage("Enter New Last Name:");
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Enter New First Name:");
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);

        view.printMessage("Enter New Middle Name:");
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        studentService.changeName(
                student.getId(),
                lastName,
                firstName,
                middleName
        );
    }

    // ================= SHOW ALL =================

    public void showStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            view.printMessage("No students registered in the system.");
            return;
        }

        view.printList(students);
    }

    // ================= SEARCH =================

    public void findStudentsByFullName() {
        view.printMessage("Enter name or part of the name to search:");
        String query = ConsoleInputValidator.readNonEmptyString(scanner);

        List<Student> result = studentService.findByFullName(query);

        if (result.isEmpty()) {
            view.printMessage("No matching students found.");
        } else {
            view.printList(result);
        }
    }
}