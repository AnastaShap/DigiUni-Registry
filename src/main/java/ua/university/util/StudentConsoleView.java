package ua.university.util;
import ua.university.domain.Student;
import java.util.List;

public class StudentConsoleView{

    private void printShort(Student s) {
        System.out.printf(
                "%s | id=%s | курс %d | група %s%n",
                s.getFullName(),
                s.getId(),
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
        System.out.println("ID: " + s.getId());
        System.out.println("Course: " + s.getCourse());
        System.out.println("Group: " + s.getGroup());
        System.out.println("Status: " + s.getStatus());
    }

    public void printMessage(String s) {
        System.out.println(s);
    }
}
