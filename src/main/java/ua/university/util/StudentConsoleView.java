package ua.university.util;
import ua.university.domain.Student;
import ua.university.service.StudentService;

import java.util.List;
import java.util.function.Function;

public class StudentConsoleView{

    private final Function<Student, Integer> ageCalculator;

    public StudentConsoleView(Function<Student, Integer> ageCalculator) {
        this.ageCalculator = ageCalculator;
    }

    private void printShort(Student s) {
        System.out.printf(
                "%s | id=%s | вік %d | курс %d | група %s%n",
                s.getFullName(),
                s.getId(),
                ageCalculator.apply(s),
                s.getCourse(),
                s.getGroup()
        );
    }

    public void printList(List<Student> students) {
        students.forEach(this::printShort);
    }

    public void printDetails(Student s) {
        System.out.println("=== Student ===");
        System.out.println("Name: " + s.getFullName());
        System.out.println("Age: " + ageCalculator.apply(s));
        System.out.println("Contacts: " + s.getEmail() + ", " + s.getPhone());
        System.out.println("ID: " + s.getId());
        System.out.println("Course: " + s.getCourse());
        System.out.println("Group: " + s.getGroup());
        System.out.println("Status: " + s.getStatus());
        System.out.println("Enty year: " + s.getEntryYear());
    }

    public void printMessage(String s) {
        System.out.println(s);
    }
}
