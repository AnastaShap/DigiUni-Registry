package ua.university.ui.student;

import ua.university.domain.Student;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.StudentConsoleView;

import java.util.List;
import java.util.Scanner;

public class StudentSearchAndReportManager {
    private final StudentService studentService;
    private final StudentConsoleView view;
    private final Scanner scanner;

    public StudentSearchAndReportManager(StudentService studentService, StudentConsoleView view, Scanner scanner) {
        this.studentService = studentService;
        this.view = view;
        this.scanner = scanner;
    }

    public void showAllStudentsMenu() {
        view.printMessage("""
                --- List Students Options ---
                1 - Standard List (Short View)
                2 - Full Details (Extended View)
                3 - Sorted by Name
                4 - Sorted by Course
                0 - Back
                """);

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 4);
        List<Student> students = switch (choice) {
            case 1, 2 -> studentService.getAllStudents();
            case 3 -> studentService.getStudentsSortedByName();
            case 4 -> studentService.getStudentsSortedByCourse();
            default -> null;
        };

        if (students == null || students.isEmpty()) {
            view.printMessage("No records found.");
            return;
        }

        // Utilizing your StudentConsoleView methods
        if (choice == 2) {
            students.forEach(view::printDetails); // Full view
        } else {
            view.printList(students); // Short view
        }
    }

    public void showSearchMenu() {
        view.printMessage("""
                --- Search & Filter ---
                1 - Search by Name
                2 - Filter by Course
                3 - Filter by Group
               
                0 - Back
                """);

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 4);
        switch (choice) {
            case 1 -> {
                view.printMessage("Enter name part:");
                String query = ConsoleInputValidator.readNonEmptyString(scanner);
                view.printList(studentService.findByFullName(query));
            }
            case 2 -> {
                int course = ConsoleInputValidator.readCourse(scanner);
                List<Student> st = studentService.findByCourse(course);
                view.printList(st);
            }
            case 3 -> {
                String group = ConsoleInputValidator.readGroup(scanner);
                view.printList(studentService.findByGroup(group));
            }
            // (4 - Filter by Department)

        }
        // /Приклад використання  методу findBy з лямбдою:
        //List<Student> seniors = studentService.findBy(s -> s.getCourse() >= 4);
    }


}
