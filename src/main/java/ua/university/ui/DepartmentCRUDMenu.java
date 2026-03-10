package ua.university.ui;

import ua.university.domain.Department;
import ua.university.domain.Faculty;
import ua.university.service.DepartmentService;
import ua.university.service.FacultyService;
import ua.university.util.ConsoleInputValidator;
import ua.university.util.ILogger;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DepartmentCRUDMenu {
    private final DepartmentService departmentService;
    private final FacultyService facultyService;
    private final ILogger logger;
    private final Scanner scanner;

    public DepartmentCRUDMenu(DepartmentService departmentService, FacultyService facultyService, ILogger logger, Scanner scanner) {
        this.departmentService = departmentService;
        this.facultyService = facultyService;
        this.logger = logger;
        this.scanner = scanner;
    }

    public void createDepartment() {
        logger.info("=== Create New Department ===");

        logger.info("Enter Department Code:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Department Name:");
        String name = ConsoleInputValidator.readNonEmptyString(scanner);

        logger.info("Enter Faculty Code:");
        String facultyCode = ConsoleInputValidator.readNonEmptyString(scanner);

        Optional<Faculty> facultyOptional = facultyService.findByCode(facultyCode);
        if (facultyOptional.isEmpty()) {
            logger.info("Error: Faculty with code " + facultyCode + " not found.");
            return;
        }

        logger.info("Enter Department Location:");
        String location = ConsoleInputValidator.readNonEmptyString(scanner);

        Department department = new Department(code, name, facultyOptional.get(), null, location);

        try {
            departmentService.create(department);
            facultyService.addDepartment(facultyCode, department);
            logger.info("Department created successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void showDepartments() {
        List<Department> departments = departmentService.findAll();
        if (departments.isEmpty()) {
            logger.info("No departments found.");
            return;
        }

        logger.info("=== Departments ===");
        departments.forEach(d -> logger.info(d.toString()));
    }

    public void deleteDepartment() {
        logger.info("Enter Department Code to delete:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        try {
            departmentService.delete(code);
            logger.info("Department deleted successfully.");
        } catch (Exception e) {
            logger.info("Error: " + e.getMessage());
        }
    }

    public void updateDepartment() {
        logger.info("Enter Department Code to update:");
        String code = ConsoleInputValidator.readNonEmptyString(scanner);

        Optional<Department> optionalDepartment = departmentService.findByCode(code);
        if (optionalDepartment.isEmpty()) {
            logger.info("Department not found.");
            return;
        }

        Department department = optionalDepartment.get();
        logger.info("Current department: " + department);
        logger.info("1-Name, 2-Location, 3-Faculty, 0-Exit");

        int choice = ConsoleInputValidator.readMenuOption(scanner, 0, 3);
        switch (choice) {
            case 1 -> {
                logger.info("Enter new department name:");
                departmentService.changeName(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Department updated.");
            }
            case 2 -> {
                logger.info("Enter new department location:");
                departmentService.changeLocation(code, ConsoleInputValidator.readNonEmptyString(scanner));
                logger.info("Department updated.");
            }
            case 3 -> {
                logger.info("Enter new faculty code:");
                String facultyCode = ConsoleInputValidator.readNonEmptyString(scanner);
                Optional<Faculty> facultyOptional = facultyService.findByCode(facultyCode);
                if (facultyOptional.isEmpty()) {
                    logger.info("Error: Faculty with code " + facultyCode + " not found.");
                    return;
                }
                departmentService.changeFaculty(code, facultyOptional.get());
                logger.info("Department updated.");
            }
            default -> {
            }
        }
    }
}
