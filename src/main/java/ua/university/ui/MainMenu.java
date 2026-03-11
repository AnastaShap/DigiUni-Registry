package ua.university.ui;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.enums.Role;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.exception.AccessDeniedException;
import ua.university.repository.IRepository;
import ua.university.repository.InMemoryDepartmentRepository;
import ua.university.repository.InMemoryFacultyRepository;
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

import java.time.LocalDate;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {
    private final DepartmentService departmentService;
    private final FacultyService facultyService;
    private final StudentService studentService;

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
        this.studentService = new StudentService(repo);

        IRepository<Faculty, String> faculRepo = new InMemoryFacultyRepository();
        this.facultyService = new FacultyService(faculRepo);

        IRepository<Department, String> depRepo = new InMemoryDepartmentRepository();
        this.departmentService = new DepartmentService(depRepo);

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

    // TEST DATA

    private void seedData() {
        // Факультети
        Faculty fit = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(fit);

        Faculty fgsn = new Faculty("FGSN", "Faculty of Humanities", "FGSN", null, "fgsn@ukma.edu.ua");
        facultyService.create(fgsn);

        Faculty fe = new Faculty("FE", "FACULTY OF ECONOMICS", "FGSN", null, "fgsn@ukma.edu.ua");
        facultyService.create(fe);

        // Кафедри
        Department informatics = new Department("INF", "Informatics", fit, null, "Building 1");
        departmentService.create(informatics);
        facultyService.addDepartment("FIT", informatics);

        Department depMath = new Department("MATH", "Math department", fit, null, "Building 1");
        departmentService.create(depMath);
        facultyService.addDepartment("FIT", depMath);

        Department finance = new Department("FIN", "Finance", fit, null, "Building 1");
        departmentService.create(finance);
        facultyService.addDepartment("FE", finance);

        // Студентів
        Student student1 = new Student(
                "1", "Шевченко", "Іван", "Петрович",
                LocalDate.of(2003, 5, 10),
                "ivan@ukma.edu.ua", "0500000001",
                "S001", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student1);
        student1.setDepartment(informatics);

        Student student4 = new Student(
                "1", "Ткач", "Марк", "Олександрович",
                LocalDate.of(2003, 4, 17),
                "ivan@ukma.edu.ua", "0500000002",
                "S004", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student4);
        student1.setDepartment(informatics);

        Student student2 = new Student(
                "2", "Коваленко", "Анна", "Олегівна",
                LocalDate.of(2004, 3, 20),
                "anna@ukma.edu.ua", "0500000002",
                "S002", 1, "AВІС-1",
                2023, StudyForm.CONTRACT, StudentStatus.STUDYING
        );
        studentService.create(student2);
        student1.setDepartment(informatics);

        Student student3 = new Student(
                "3", "Бондар", "Максим", "Ігорович",
                LocalDate.of(2002, 11, 2),
                "max@ukma.edu.ua", "0500000003",
                "S003", 2, "КН-2",
                2021, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student3);
        student1.setDepartment(informatics);
    }
}
