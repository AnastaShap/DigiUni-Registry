package ua.university.ui;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.service.StudentService;
import ua.university.util.ILogger;
import ua.university.util.ConsoleInputValidator;

import java.util.List;
import java.util.Scanner;

//import static ua.university.util.ConsoleInputValidator.readNonEmptyString;

public class StudentMenu {

    private final StudentService studentService;
    private final ILogger logger;
    private final Scanner scanner;

    public StudentMenu(StudentService studentService, ILogger logger, Scanner scanner) {
        this.studentService = studentService;
        this.logger = logger;
        this.scanner = scanner;
    }

    // --- CREATE ---

    public void createStudent() {
        System.out.println("=== Create Student ===");

        //String id = ConsoleInputValidator.readNonEmptyString(scanner);
        /*String lastName = readNonEmptyString("Enter Last Name: ");
        String firstName = readNonEmptyString("Enter First Name: ");
        String middleName = readNonEmptyString("Enter Middle Name: ");
        int year = intInRange("Enter birth year: ", 1900, LocalDate.now().getYear());
        int month = intInRange("Enter birth month: ", 1, 12);
        int day = intInRange("Enter birth day: ", 1, 31);
        LocalDate birthDate = LocalDate.of(year, month, day);
        String email = readNonEmptyString("Enter Email: ");
        String phone = readNonEmptyString("Enter Phone: ");

        String studentId = readNonEmptyString("Enter Student ID: ");
        int course = intInRange("Enter course (1-6): ", 1, 6);
        String faculty = readNonEmptyString(scanner);

        String group = readNonEmptyString("Enter group: ");
        int entryYear = intInRange("Enter entry year: ", 2000, LocalDate.now().getYear());

        System.out.println("1 - Budget\n2 - Contract");
        int choice = intInRange("Your choice: ", 1, 2);
        StudyForm form = choice == 1 ? StudyForm.BUDGET : StudyForm.CONTRACT;

        System.out.println("1 - Studying\n2 - Academic leave\n3 - Expelled");
        int choice2 = intInRange("Your choice: ", 1, 3);
        StudentStatus status = switch (choice2) {
            case 1 -> StudentStatus.STUDYING;
            case 2 -> StudentStatus.ACADEMIC_LEAVE;
            default -> StudentStatus.EXPELLED;
        };

        Student s = new Student(id); //lastName, firstName, middleName, birthDate, email, phone,
                //studentId, course, group, entryYear, form, status);

        // here is business-logic usage-> Student Service
        studentService.create(s);
        System.out.println("Student registered successfully!");*/

        /*if (created) {
            System.out.println("Student registered successfully!");
        } else {
            System.out.println("Student with this ID already exists.");
        }*/

        // --test --
        String id = ConsoleInputValidator.readNonEmptyString(scanner);
        String lastName = ConsoleInputValidator.readNonEmptyString(scanner);
        String firstName = ConsoleInputValidator.readNonEmptyString(scanner);
        String middleName = ConsoleInputValidator.readNonEmptyString(scanner);

        int course = ConsoleInputValidator.readCourse(scanner);
        String group = ConsoleInputValidator.readGroup(scanner);

        Student student = new Student(
                id,
                lastName,
                firstName,
                middleName,
                null, null, null,
                "S" + id,
                course,
                group,
                2023,
                StudyForm.BUDGET,
                StudentStatus.STUDYING
        );
        studentService.create(student);
        logger.info("Student created successfully.");
    }

    // --- DELETE ---

    public void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        String id = scanner.nextLine();

        studentService.delete(id);
        logger.info("Delete operation completed.");
    }

    // --- UPDATE ---

    public void updateStudent() {/*
        System.out.print("Enter ID to update: ");
        String id = scanner.nextLine();

        Optional<Student> optionalStudent = studentService.findById(id);

        if (optionalStudent.isEmpty()) {
            logger.info("Student not found.");
            return;
        }

        Student s = optionalStudent.get();

        System.out.println("Updating: " + s.getFullName());
        System.out.println("1-ID 2-Name 3-Birthdate 4-Email 5-Phone 6-Faculty 7-Course 8-Group 9-EntryYear 10-StudyForm 11-Status 0-Exit");
        int choice = InputValidator.readMenuOption(scanner, 0, 10);

        switch (choice) {
            case 1 -> s.setId(readNonEmptyString(scanner));
            case 2 -> {
                s.setLastName(readNonEmptyString("New Last Name: "));
                s.setFirstName(readNonEmptyString("New First Name: "));
                s.setMiddleName(readNonEmptyString("New Middle Name: "));
            }
            case 3 -> {
                int year = intInRange("Enter birth year: ", 1900, LocalDate.now().getYear());
                int month = intInRange("Enter birth month: ", 1, 12);
                int day = intInRange("Enter birth day: ", 1, 31);
                s.setBirthDate(LocalDate.of(year, month, day));
            }
            case 4 -> s.setEmail(readNonEmptyString("New Email: "));
            case 5 -> s.setPhone(readNonEmptyString("New Phone: "));
            case 6 -> {
                int newCourse = InputValidator.readCourse(scanner);
                s.setCourse(newCourse);
            }
            case 7 -> {
                String newGroup = InputValidator.readGroup(scanner);
                s.setGroup(newGroup);
            }
            case 0 -> {
                return;
            }
        }

        studentService.update(s);
        logger.info("Student updated successfully.");*/
    }

    // ---SHOW---
    public void showStudents() {


    }

    // --- SEARCH ---

    public void findStudentsByFullName() {
        String query = ConsoleInputValidator.readNonEmptyString(scanner);
        List<Student> result = studentService.findByFullName(query);
        logSearchResult(result, "No students found.");
    }

    private void logSearchResult(List<Student> students, String emptyMessage) {
        if (students.isEmpty()) {
            logger.info(emptyMessage);
        } else {
            logger.logStudents(students);
        }
    }



}
