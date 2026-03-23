package ua.university.ui;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.domain.Student;
import ua.university.domain.Teacher;
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

        this.studentMenu = new StudentCRUDMenu(studentService, departmentService, facultyService, logger, scanner);
        this.facultyMenu = new FacultyCRUDMenu(facultyService, logger, scanner);
        this.departmentMenu = new DepartmentCRUDMenu(departmentService, facultyService, logger, scanner);

        seedData();
    }

    public void run() {
        login();
        while (true) {
            printMenu();
            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 14);
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
                    case 14 -> login();
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
        System.out.println("14- Change Current Role");
        System.out.println("0- Exit");
    }

    // ===TEST DATA===

    private void seedData() {
        // ВСІ Факультети НаУКМА
       // seedFaculties();
        Faculty fit = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(fit);

        Faculty fgn = new Faculty("FGN", "Faculty of Humanities", "FGN", null, "fgn@ukma.edu.ua");
        facultyService.create(fgn);

        Faculty fen = new Faculty("FEN", "Faculty of Economics", "FEN", null, "fen@ukma.edu.ua");
        facultyService.create(fen);

        //  Факультет правничих наук (ФПрН)
        Faculty fpn = new Faculty("FPN", "Faculty of Law", "FPN", null, "fpn@ukma.edu.ua");
        facultyService.create(fpn);

        //  Факультет природничих наук (ФПН)
        Faculty fpsn = new Faculty("FPSN", "Faculty of Natural Sciences", "FPSN", null, "fpsn@ukma.edu.ua");
        facultyService.create(fpsn);

        //  Факультет соціальних наук і соціальних технологій (ФСНСТ)
        Faculty fsnst = new Faculty("FSNST", "Faculty of Social Sciences", "FSNST", null, "fsnst@ukma.edu.ua");
        facultyService.create(fsnst);

        //  Факультет охорони здоров’я, соціальної роботи та психології (ФОЗ)
        Faculty foz = new Faculty("FOZ", "Faculty of Health Sciences", "FOZ", null, "foz@ukma.edu.ua");
        facultyService.create(foz);

         // ДЕКАНАТ ФІ
        Teacher deanFit = new Teacher("DFIT", "Глибовець", "Андрій", "Миколайович",
                LocalDate.of(1978, 1, 1), "a.glybovets@ukma.edu.ua", "+380444636985",
                "Professor", "Dr. Sc.", "Academician", LocalDate.of(1990, 9, 1), 1.0);
        facultyService.assignDean("FIT", deanFit);

        // DEPARTMENTS
        // --- Кафедри ФІТ (Факультет інформаційних технологій) ---
        Department informatics = new Department("INF", "Кафедра інформатики", fit, deanFit, "1-й корпус");
        departmentService.create(informatics);
        facultyService.addDepartment("FIT", informatics);

        Department maths = new Department("MATH", "Кафедра математики", fit, null, "1-й корпус");
        departmentService.create(maths);
        facultyService.addDepartment("FIT", maths);

        Department networkTech = new Department("NT", "Кафедра мережевих технологій", fit, null, "1-й корпус");
        departmentService.create(networkTech);
        facultyService.addDepartment("FIT", networkTech);

// --- Кафедри ФЕН (Факультет економічних наук) ---
        Department finance = new Department("FIN", "Кафедра фінансів", fen, null, "6-й корпус");
        departmentService.create(finance);
        facultyService.addDepartment("FEN", finance);

        Department marketing = new Department("MKT", "Кафедра маркетингу", fen, null, "6-й корпус");
        departmentService.create(marketing);
        facultyService.addDepartment("FEN", marketing);

        Department economics = new Department("ECON", "Кафедра економічної теорії", fen, null, "6-й корпус");
        departmentService.create(economics);
        facultyService.addDepartment("FEN", economics);

// --- Кафедри ФГН (Факультет гуманітарних наук) ---
        Department history = new Department("HIST", "Кафедра історії", fgn, null, "4-й корпус");
        departmentService.create(history);
        facultyService.addDepartment("FGN", history);

        Department philology = new Department("PHIL", "Кафедра філології", fgn, null, "4-й корпус");
        departmentService.create(philology);
        facultyService.addDepartment("FGN", philology);

// --- Кафедри ФПрН (Факультет правничих наук) ---
        Department law = new Department("LAW", "Кафедра правознавства", fpn, null, "4-й корпус");
        departmentService.create(law);
        facultyService.addDepartment("FPN", law);

        // -- Студенти ФІ --
        Student student1 = new Student(
                "100001", "Шевченко", "Іван", "Петрович",
                LocalDate.of(2003, 5, 10),
                "ivanshevch@ukma.edu.ua", "0999134159",
                "S001", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student1);
        student1.setDepartment(informatics);

        Student student4 = new Student(
                "1000004", "Ткач", "Олексій", "Олександрович",
                LocalDate.of(2003, 4, 17),
                "tkacholex@ukma.edu.ua", "0637099723",
                "S004", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student4);
        student1.setDepartment(informatics);

        Student student2 = new Student(
                "100002", "Коваленко", "Анна", "Олегівна",
                LocalDate.of(2004, 3, 20),
                "anna@ukma.edu.ua", "0999120869",
                "S002", 1, "AВІС-1",
                2023, StudyForm.CONTRACT, StudentStatus.STUDYING
        );
        studentService.create(student2);
        student1.setDepartment(informatics);

        Student student3 = new Student(
                "140004", "Бондар", "Максим", "Ігорович",
                LocalDate.of(2002, 11, 2),
                "max@ukma.edu.ua", "0637099418",
                "S003", 2, "КН-2",
                2021, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student3);
        student1.setDepartment(informatics);
    }

   /* private void seedFaculties(){
        Faculty fit = new Faculty("FIT", "Faculty of IT", "FIT", null, "fit@ukma.edu.ua");
        facultyService.create(fit);

        Faculty fgn = new Faculty("FGN", "Faculty of Humanities", "FGN", null, "fgn@ukma.edu.ua");
        facultyService.create(fgn);

        Faculty fen = new Faculty("FEN", "Faculty of Economics", "FEN", null, "fen@ukma.edu.ua");
        facultyService.create(fen);

        //  Факультет правничих наук (ФПрН)
        Faculty fpn = new Faculty("FPN", "Faculty of Law", "FPN", null, "fpn@ukma.edu.ua");
        facultyService.create(fpn);

        //  Факультет природничих наук (ФПН)
        Faculty fpsn = new Faculty("FPSN", "Faculty of Natural Sciences", "FPSN", null, "fpsn@ukma.edu.ua");
        facultyService.create(fpsn);

        //  Факультет соціальних наук і соціальних технологій (ФСНСТ)
        Faculty fsnst = new Faculty("FSNST", "Faculty of Social Sciences", "FSNST", null, "fsnst@ukma.edu.ua");
        facultyService.create(fsnst);

        //  Факультет охорони здоров’я, соціальної роботи та психології (ФОЗ)
        Faculty foz = new Faculty("FOZ", "Faculty of Health Sciences", "FOZ", null, "foz@ukma.edu.ua");
        facultyService.create(foz);
    }*/
}
