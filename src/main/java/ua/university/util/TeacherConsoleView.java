package ua.university.util;

import ua.university.domain.Teacher;
import java.util.List;

public class TeacherConsoleView {
    public void printShort(Teacher t) {
        System.out.printf("ID: %-5s | ПІБ: %-25s | Посада: %-15s | Ступінь: %s%n",
                t.getId(), t.getFullName(), t.getPosition(), t.getDegree());
    }

    public void printList(List<Teacher> teachers) {
        System.out.println("=== List of teachers ===");
        teachers.forEach(this::printShort);
    }

    public void printDetails(Teacher t) {
        System.out.println("=== Teacher Details ===");
        System.out.println("ID: " + t.getId());
        System.out.println("Full Name: " + t.getFullName());
        System.out.println("Birth Date: " + t.getBirthDate());
        System.out.println("Contacts: " + t.getEmail() + ", " + t.getPhone());
        System.out.println("Position: " + t.getPosition());
        System.out.println("Degree: " + t.getDegree());
        System.out.println("Academic Title: " + t.getAcademicTitle());
        System.out.println("Hire Date: " + t.getHireDate());
        System.out.println("Workload: " + t.getWorkload());
    }
}
