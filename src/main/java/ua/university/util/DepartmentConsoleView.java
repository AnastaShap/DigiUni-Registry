package ua.university.util;

import ua.university.domain.Department;
import java.util.List;

public class DepartmentConsoleView {
    public void printShort(Department d) {
        String facultyInfo = (d.getFaculty() != null) ? d.getFaculty().getShortName() : "No Faculty";
        System.out.printf("Code: %-5s | Name: %-25s | Faculty: %-10s | Location: %s%n",
                d.getCode(), d.getName(), facultyInfo, d.getLocation()); //
    }

    public void printList(List<Department> departments) {
        System.out.println("=== Department List ===");
        departments.forEach(this::printShort);
    }

    public void printDetails(Department d) {
        System.out.println("=== Department Details ===");
        System.out.println("Code: " + d.getCode());
        System.out.println("Name: " + d.getName());
        System.out.println("Faculty: " + (d.getFaculty() != null ? d.getFaculty().getName() : "None"));
        System.out.println("Head: " + (d.getHead() != null ? d.getHead().getFullName() : "Not assigned"));
        System.out.println("Location: " + d.getLocation());
    }
}