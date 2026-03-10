package ua.university.ui;

import ua.university.repository.student.InMemoryStudentRepository;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
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
    private final FacultyCRUDMenu facultyMenu;
    private final DepartmentCRUDMenu departmentMenu;
    private final Scanner scanner;

    public MainMenu(ILogger logger) {
        this.scanner = new Scanner(System.in);

        InMemoryStudentRepository repo = new InMemoryStudentRepository();
        StudentService studentService = new StudentService(repo);
        FacultyService facultyService = new FacultyService();
        DepartmentService departmentService = new DepartmentService();
        this.studentMenu = new StudentCRUDMenu(studentService, departmentService, logger, scanner);
        this.facultyMenu = new FacultyCRUDMenu(facultyService, logger, scanner);
        this.departmentMenu = new DepartmentCRUDMenu(departmentService, facultyService, logger, scanner);
        // Only for demo test
        //repo.loadTestData(repo);
    }

    public void run() {
        while (true) {
            printMenu();
            int option = ConsoleInputValidator.readMenuOption(scanner, 0, 13);
            switch (option) {
                case 1 -> studentMenu.createStudent();
                case 2 -> studentMenu.showStudents();
                case 3 -> studentMenu.updateStudent();
                case 4 -> studentMenu.deleteStudent();
                case 5 -> studentMenu.searchMenu();
                case 6 -> facultyMenu.createFaculty();
                case 7 -> facultyMenu.showFaculties();
                case 8 -> facultyMenu.updateFaculty();
                case 9 -> facultyMenu.deleteFaculty();
                case 10 -> departmentMenu.createDepartment();
                case 11 -> departmentMenu.showDepartments();
                case 12 -> departmentMenu.updateDepartment();
                case 13 -> departmentMenu.deleteDepartment();
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
           5- Search/Filter Students
           6- Add Faculty
           7- List Faculties
           8- Update Faculty
           9- Delete Faculty
           10- Add Department
           11- List Departments
           12- Update Department
           13- Delete Department
           0- Exit""");
    }

}
