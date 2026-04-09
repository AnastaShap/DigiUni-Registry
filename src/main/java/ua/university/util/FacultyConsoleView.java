package ua.university.util;

import ua.university.domain.Faculty;
import ua.university.domain.Teacher;
import java.util.List;

public class FacultyConsoleView {
    public void printShort(Faculty f) {
        System.out.printf("Code: %-6s | Short: %-8s | Name: %-30s%n",
                f.getCode(), f.getShortName(), f.getName());
    }

    public void printList(List<Faculty> faculties) {
        System.out.println("=== Faculty List ===");
        faculties.forEach(this::printShort);
    }

    public void printDetails(Faculty f) {
        System.out.println("=== Faculty Details ===");
        System.out.println("Code: " + f.getCode());
        System.out.println("Full Name: " + f.getName());
        System.out.println("Short Name: " + f.getShortName());

        // ВИПРАВЛЕННЯ: Використовуємо map для безпечного доступу до ПІБ декана
        String deanName = f.getDean()
                .map(Teacher::getFullName)
                .orElse("Not assigned");

        System.out.println("Dean: " + deanName);
        System.out.println("Contacts: " + f.getContacts());
    }
}