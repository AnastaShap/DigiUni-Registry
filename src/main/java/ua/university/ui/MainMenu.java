package ua.university.ui;

import ua.university.domain.enums.Role;
import ua.university.exception.AccessDeniedException;
import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.security.AccessManager;
import ua.university.security.AuthService;
import ua.university.security.User;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
import ua.university.service.StudentService;
import ua.university.ui.student.StudentCRUDMenu;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;

import java.util.Scanner;
import java.util.Set;

public class MainMenu {
    private final StudentCRUDMenu studentMenu;
    private final FacultyCRUDMenu facultyMenu;
    private final DepartmentCRUDMenu departmentMenu;
    private final Scanner scanner;
    private final AuthService authService;
    private final AccessManager accessManager;
    private User currentUser;

    public MainMenu(ILogger logger) {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
        this.accessManager = new AccessManager();

        InMemoryStudentRepository repo = new InMemoryStudentRepository();
        StudentService studentService = new StudentService(repo);
        FacultyService facultyService = new FacultyService();
        DepartmentService departmentService = new DepartmentService();
        this.studentMenu = new StudentCRUDMenu(studentService, departmentService, logger, scanner);
        this.facultyMenu = new FacultyCRUDMenu(facultyService, logger, scanner);
        this.departmentMenu = new DepartmentCRUDMenu(departmentService, facultyService, logger, scanner);
    }

    public void run() {
        login();
        while (true) {
            printMenu();
            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 13);
            try {
                switch (option) {
                    case 1 -> studentMenu.createStudent();
                    case 2 -> studentMenu.showStudents();
                    case 3 -> studentMenu.updateStudent();
                    case 4 -> studentMenu.deleteStudent();
                    case 5 -> studentMenu.searchMenu();
                    case 6 -> { requireManager(); facultyMenu.createFaculty(); }
                    case 7 -> facultyMenu.showFaculties();
                    case 8 -> { requireManager(); facultyMenu.updateFaculty(); }
                    case 9 -> { requireManager(); facultyMenu.deleteFaculty(); }
                    case 10 -> { requireManager(); departmentMenu.createDepartment(); }
                    case 11 -> departmentMenu.showDepartments();
                    case 12 -> { requireManager(); departmentMenu.updateDepartment(); }
                    case 13 -> { requireManager(); departmentMenu.deleteDepartment(); }
                    case 0 -> { return; }
                }
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void login() {
        System.out.print("Login (admin -> MANAGER, any other login -> USER): ");
        String login = scanner.nextLine().trim();
        currentUser = authService.login(login);
        System.out.println("Logged in as " + currentUser.login() + " with role " + currentUser.role());
    }

    private void requireManager() {
        accessManager.requireAnyRole(currentUser, Set.of(Role.MANAGER));
    }

    private void printMenu() {
        System.out.println("Current role: " + (currentUser != null ? currentUser.role() : "UNKNOWN"));
        System.out.println("1- Add Student");
        System.out.println("2- List/Sort Students");
        System.out.println("3- Update Student");
        System.out.println("4- Delete Student");
        System.out.println("5- Search/Filter Students");
        System.out.println("6- Add Faculty (manager)");
        System.out.println("7- List Faculties");
        System.out.println("8- Update Faculty (manager)");
        System.out.println("9- Delete Faculty (manager)");
        System.out.println("10- Add Department (manager)");
        System.out.println("11- List Departments");
        System.out.println("12- Update Department (manager)");
        System.out.println("13- Delete Department (manager)");
        System.out.println("0- Exit");
    }
}
