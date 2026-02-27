package ua.university.ui;

import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.service.StudentService;
import ua.university.ui.student.StudentCRUDMenu;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;

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
    private final StudentCRUDMenu studentMenu;
    private final Scanner scanner;

    public MainMenu(ILogger logger) {
        this.scanner = new Scanner(System.in);

        InMemoryStudentRepository repo = new InMemoryStudentRepository();
        StudentService studentService = new StudentService(repo);
        this.studentMenu = new StudentCRUDMenu(studentService, logger, scanner);
        repo.loadTestData(repo);
    }

    public void run() {
        while (true) {
            printMenu();
            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 5);
            switch (option) {
                case 1 -> studentMenu.createStudent();
                case 2 -> studentMenu.showStudents();
                case 3 -> studentMenu.updateStudent();
                case 4 -> studentMenu.deleteStudent();
                case 5 -> studentMenu.searchMenu();
                case 0 -> { return; }
            }
        }
    }

    private void printMenu() {
        System.out.println("""
           1- Add Student
           2- List/Sort Students
           3- Update Student
           4- Delete Student
           5- Search/Filter
           0- Exit""");
    }

}
