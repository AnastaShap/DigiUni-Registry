package ua.university.ui;

import ua.university.domain.*;
import ua.university.domain.enums.Role;
import ua.university.domain.enums.StudentStatus;
import ua.university.domain.enums.StudyForm;
import ua.university.dto.Email;
import ua.university.dto.PhoneNumber;
import ua.university.exception.AccessDeniedException;
import ua.university.io.DataStorageService;
import ua.university.io.UniversityDataSnapshot;
import ua.university.repository.InMemoryDepartmentRepository;
import ua.university.repository.InMemoryFacultyRepository;
import ua.university.repository.InMemoryTeacherRepository;
import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.security.AccessManager;
import ua.university.security.AuthService;
import ua.university.security.User;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
import ua.university.service.StudentService;
import ua.university.service.TeacherService;
import ua.university.service.multithreading.AutoSaveService;
import ua.university.ui.faculty.FacultyCRUDMenu;
import ua.university.ui.student.StudentCRUDMenu;
import ua.university.ui.teacher.TeacherCRUDMenu;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.Logging.ILogger;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Set;


public class MainMenu {
    private final DepartmentService departmentService;
    private final FacultyService facultyService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    private final StudentCRUDMenu studentMenu;
    private final FacultyCRUDMenu facultyMenu;
    private final DepartmentCRUDMenu departmentMenu;
    private final TeacherCRUDMenu teacherMenu;
    private final Scanner scanner;
    private final AuthService authService;
    private final AccessManager accessManager;
    private User currentUser;

    private final DataStorageService dataStorageService;
    private final Path dataFile;

    private final AutoSaveService autoSaveService;


    public MainMenu(ILogger logger) {
        this.authService = new AuthService();
        this.dataStorageService = new DataStorageService();
        this.dataFile = Path.of("data", "university-data.bin");
        this.scanner = new Scanner(System.in);

        this.accessManager = new AccessManager();

        // Ініціалізація репозиторіїв та сервісів
        this.studentService = new StudentService(new InMemoryStudentRepository());
        this.facultyService = new FacultyService(new InMemoryFacultyRepository());
        this.departmentService = new DepartmentService(new InMemoryDepartmentRepository());
        this.teacherService = new TeacherService(new InMemoryTeacherRepository());


        this.facultyMenu = new FacultyCRUDMenu(facultyService, logger, scanner, authService);
        this.departmentMenu = new DepartmentCRUDMenu(departmentService, facultyService, logger, scanner, authService);
        this.studentMenu = new StudentCRUDMenu(studentService, departmentService, facultyService, logger, scanner);
        this.teacherMenu = new TeacherCRUDMenu(teacherService, logger, scanner, authService);

        this.autoSaveService = new AutoSaveService(
                this.dataStorageService,
                this.dataFile,
                this.facultyService,
                this.departmentService,
                this.studentService,
                this.teacherService,
                logger

        );

        loadOrSeedData();
        this.autoSaveService.startAutoSave(60);
    }

    private void loadOrSeedData() {
        if (dataStorageService.exists(dataFile)) {
            UniversityDataSnapshot snapshot = dataStorageService.load(dataFile);
            if (snapshot != null) {
                snapshot.faculties().forEach(facultyService::update);
                snapshot.departments().forEach(departmentService::update);
                snapshot.students().forEach(studentService::update);
                snapshot.teachers().forEach(teacherService::update);

                System.out.println("Data loaded from file.");
                return;
            }
        }
        seedData(); // Виконається тільки якщо файлу немає
    }

    private void saveData() {
        UniversityDataSnapshot snapshot = new UniversityDataSnapshot(
                facultyService.findAll(),
                departmentService.findAll(),
                studentService.getAllStudents(),
                teacherService.getAll()
        );

        dataStorageService.save(dataFile, snapshot);
        System.out.println("Data saved.");
    }

    public void run() {
        login();

        while (true) {
            printMenu();
            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 21);

            try {
                switch (option) {
                    case 1 -> {
                        requireManager();
                        studentMenu.createStudent();
                    }
                    case 2 -> studentMenu.showStudents();
                    case 3 -> {
                        requireManager();
                        studentMenu.updateStudent();
                    }
                    case 4 -> {
                        requireManager();
                        Person user = authService.getCurrentUser();
                        studentMenu.deleteStudent(user);
                    }
                    case 5 -> studentMenu.searchMenu();

                    case 6 -> {
                        requireManager();
                        facultyMenu.createFaculty();
                    }
                    case 7 -> facultyMenu.showFaculties();
                    case 8 -> {
                        requireManager();
                        facultyMenu.updateFaculty();
                    }
                    case 9 -> {
                        requireManager();
                        facultyMenu.deleteFaculty();
                    }

                    case 10 -> {
                        requireManager();
                        departmentMenu.createDepartment();
                    }
                    case 11 -> departmentMenu.showDepartments();
                    case 12 -> {
                        requireManager();
                        departmentMenu.updateDepartment();
                    }
                    case 13 -> {
                        requireManager();
                        departmentMenu.deleteDepartment();
                    }

                    case 14 -> login();

                    case 15 -> {
                        requireAdmin();
                        showUsers();
                    }
                    case 16 -> {
                        requireAdmin();
                        createUser();
                    }
                    case 17 -> {
                        requireAdmin();
                        changeUserRole();
                    }
                    case 18 -> {
                        requireAdmin();
                        blockOrUnblockUser();
                    }
                    case 19 -> {
                        requireManager();
                        teacherMenu.createTeacher();
                    }
                    case 20 -> teacherMenu.showTeachers();
                    case 21 -> {
                        requireManager();
                        teacherMenu.deleteTeacher();
                    }


                    case 0 -> {
                        autoSaveService.stop(); // Зупиняємо фоновий потік
                        saveData();             // Фінальне збереження перед виходом
                        return;
                    }
                }
            } catch (AccessDeniedException e) {
                System.out.println(e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void login() {
        while (true) {
            try {
                System.out.print("Login: ");
                String login = scanner.nextLine().trim();

                System.out.print("Password: ");
                String password = scanner.nextLine().trim();

                currentUser = authService.login(login, password);

                Teacher sessionPerson = new Teacher(
                        "session", "System", currentUser.getLogin(), "",
                        LocalDate.now(), null, null, "User", "None", "None", LocalDate.now(), 1.0
                );

                // Конвертуємо Role в бітові маски (Permissions)
                authService.assignPermissions(sessionPerson, currentUser.getRole());

                // Передаємо персону в AuthService, щоб canExecute не видавав null
                authService.setCurrentUser(sessionPerson);
                // --------------------------------------------------------------

                System.out.println("Успішний вхід: " + currentUser.getLogin() +
                        ", роль: " + currentUser.getRole());
                return;

            } catch (RuntimeException e) {
                System.out.println("Помилка входу: " + e.getMessage());
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    private void requireManager() {
        accessManager.requireAnyRole(currentUser, Set.of(Role.MANAGER, Role.ADMIN));
    }

    private void requireAdmin() {
        accessManager.requireAnyRole(currentUser, Set.of(Role.ADMIN));
    }

    private void printMenu() {
        System.out.println("\nCurrent user: " + (currentUser != null ? currentUser.getLogin() : "UNKNOWN"));
        System.out.println("Current role: " + (currentUser != null ? currentUser.getRole() : "UNKNOWN"));

        System.out.println("1 - Add Student (manager/admin)");
        System.out.println("2 - List/Sort Students");
        System.out.println("3 - Update Student (manager/admin)");
        System.out.println("4 - Delete Student (manager/admin)");
        System.out.println("5 - Search/Filter Students");

        System.out.println("6 - Add Faculty (manager/admin)");
        System.out.println("7 - List Faculties");
        System.out.println("8 - Update Faculty (manager/admin)");
        System.out.println("9 - Delete Faculty (manager/admin)");

        System.out.println("10 - Add Department (manager/admin)");
        System.out.println("11 - List Departments");
        System.out.println("12 - Update Department (manager/admin)");
        System.out.println("13 - Delete Department (manager/admin)");

        System.out.println("14 - Login as another user");

        System.out.println("15 - Show users (admin)");
        System.out.println("16 - Create user (admin)");
        System.out.println("17 - Change user role (admin)");
        System.out.println("18 - Block/Unblock user (admin)");

        System.out.println("19 - Add Teacher (manager/admin)");
        System.out.println("20 - List Teachers");
        System.out.println("21 - Delete Teacher (manager/admin)");

        System.out.println("0 - Exit");
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
                LocalDate.of(1978, 1, 1),
                new Email("a.glybovets@ukma.edu.ua"), new PhoneNumber("+380444636985"),
                "Professor", "Dr. Sc.", "Academician", LocalDate.of(1990, 9, 1), 1.0);
        teacherService.create(deanFit);

        Teacher vovkNat = new Teacher("T_VOVK", "Вовк", "Наталія", "Миколаївна",
                LocalDate.of(1970, 5, 12),
                new Email("vovknj@ukma.edu.ua"), new PhoneNumber("+380444256057"),
                "Старший викладач", "PhD", "Associate Professor", LocalDate.of(2005, 9, 1), 1.0);
        teacherService.create(vovkNat);

        // ФЕН
        Teacher bazhalIur = new Teacher("T_BAZHAL", "Бажал", "Юрій", "Миколайович",
                LocalDate.of(1952, 12, 1),
                new Email("bazhal@ukma.edu.ua"), new PhoneNumber("+380444256042"),
                "Завідувач кафедри", "Dr. Sc.", "Professor", LocalDate.of(1995, 9, 1), 1.0);
        teacherService.create(bazhalIur);

        Teacher hlushSvit = new Teacher("T_HLUSH", "Глущенко", "Світлана", "Василівна",
                LocalDate.of(1965, 3, 15),
                new Email("svitlana.hlushchenko@ukma.edu.ua"), new PhoneNumber("+380444257737"),
                "Декан ФЕН", "PhD", "Associate Professor", LocalDate.of(2000, 9, 1), 1.0);
        teacherService.create(hlushSvit);

        // ФПрН
        Teacher zaietsAnat = new Teacher("T_ZAIETS", "Заєць", "Анатолій", "Павлович",
                LocalDate.of(1954, 7, 20),
                new Email("zaietsap@ukma.edu.ua"), new PhoneNumber("+380444635927"),
                "Професор", "Dr. Sc.", "Professor", LocalDate.of(2010, 9, 1), 1.0);
        teacherService.create(zaietsAnat);

        // ФГН
        Teacher kobchenkoNat = new Teacher("T_KOBCHENKO", "Кобченко", "Наталія", "Віталіївна",
                LocalDate.of(1975, 10, 5),
                new Email("n.kobchenko@ukma.edu.ua"), new PhoneNumber("+380444256059"),
                "Професор", "Dr. Sc.", "Professor", LocalDate.of(2008, 9, 1), 1.0);
        teacherService.create(kobchenkoNat);

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
        // Студент 1
        Student student1 = new Student(
                "100001", "Шевченко", "Іван", "Петрович",
                LocalDate.of(2003, 5, 10),
                "ivanshevch@ukma.edu.ua", "+380999134159",
                "S001", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student1);

        Student student4 = new Student(
                "1000004", "Ткач", "Олексій", "Олександрович",
                LocalDate.of(2003, 4, 17),
                "tkacholex@ukma.edu.ua", "0637099723",
                "S004", 2, "ІПЗ-2",
                2022, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student4);

        Student student2 = new Student(
                "100002", "Коваленко", "Анна", "Олегівна",
                LocalDate.of(2004, 3, 20),
                "anna@ukma.edu.ua", "0999120869",
                "S002", 1, "AВІС-1",
                2023, StudyForm.CONTRACT, StudentStatus.STUDYING
        );
        studentService.create(student2);

        Student student3 = new Student(
                "140004", "Бондар", "Максим", "Ігорович",
                LocalDate.of(2002, 11, 2),
                "max@ukma.edu.ua", "0637099418",
                "S003", 2, "КН-2",
                2021, StudyForm.BUDGET, StudentStatus.STUDYING
        );
        studentService.create(student3);


        // Замість student1.setDepartment(informatics);
        departmentService.addStudent("INF", student1);
        departmentService.addStudent("INF", student4);
        departmentService.addStudent("INF", student2);
        departmentService.addStudent("INF", student3);
    }

   private void showUsers() {
       System.out.println("\n=== USERS ===");
       for (User user : authService.getUsers().values()) {
           System.out.println(user);
       }
   }

    private void createUser() {
        System.out.print("New login: ");
        String login = scanner.nextLine().trim();

        System.out.print("New password: ");
        String password = scanner.nextLine().trim();

        System.out.println("Role: 1 - USER, 2 - MANAGER, 3 - ADMIN");
        String roleInput = scanner.nextLine().trim();

        Role role = parseRole(roleInput);

        authService.createUser(login, password, role);
        System.out.println("Користувача створено.");
    }

    private void changeUserRole() {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();

        System.out.println("New role: 1 - USER, 2 - MANAGER, 3 - ADMIN");
        String roleInput = scanner.nextLine().trim();

        Role role = parseRole(roleInput);

        authService.changeRole(login, role);
        System.out.println("Роль змінено.");
    }

    private void blockOrUnblockUser() {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();

        System.out.println("1 - Block, 2 - Unblock");
        String action = scanner.nextLine().trim();

        if ("1".equals(action)) {
            authService.blockUser(login);
            System.out.println("Користувача заблоковано.");
        } else if ("2".equals(action)) {
            authService.unblockUser(login);
            System.out.println("Користувача розблоковано.");
        } else {
            System.out.println("Невірна дія.");
        }
    }

    private Role parseRole(String input) {
        return switch (input) {
            case "1" -> Role.USER;
            case "2" -> Role.MANAGER;
            case "3" -> Role.ADMIN;
            default -> throw new RuntimeException("Невірно вибрана роль");
        };
    }
}
