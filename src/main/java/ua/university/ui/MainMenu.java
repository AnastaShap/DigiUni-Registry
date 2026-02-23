package ua.university.ui;

import ua.university.domain.Student;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.service.StudentService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * The main menu of the console application.
 * <p>
 * Responsible for initializing the main components:
 * repository, services and submenus.
 * </p>
 * Acts as an entry point for navigating between menus.
 */

public  class MainMenu {
    private final StudentMenu studentMenu;
    private final Scanner scanner;

    public MainMenu(ILogger logger) {
        this.scanner = new Scanner(System.in);

        InMemoryStudentRepository repo = new InMemoryStudentRepository();
        StudentService studentService = new StudentService(repo);
        this.studentMenu = new StudentMenu(studentService, logger, scanner);
        repo.loadTestData(repo);
    }

    public void run() {
        while (true) {
            printMenu();

            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 4);

            switch (option) {
                case 1 -> studentMenu.createStudent();
                case 2 -> studentMenu.findStudentsByFullName();
                case 3 -> studentMenu.updateStudent();
                case 4 -> studentMenu.deleteStudent();
                case 0 -> {
                    System.out.println("Program finished.");
                    return;
                }
            }
        }
    }

    private void printMenu() {

        System.out.println("""
                    --- CRUD MENU ---
           1- Add Student
           2- List Students
           3- Update Student
           4- Delete Student
           0- Exit""");
    }

}
